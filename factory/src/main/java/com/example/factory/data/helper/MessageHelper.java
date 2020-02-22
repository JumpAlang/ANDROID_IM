package com.example.factory.data.helper;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.common.common.Common;
import com.example.common.common.app.Application;
import com.example.common.utils.PicturesCompressor;
import com.example.common.utils.StreamUtil;
import com.example.factory.BaseObserver;
import com.example.factory.Factory;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.example.factory.net.UploadHelper;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 消息工具类
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class MessageHelper {
    public static final String TAG = "MessageHelper";

    // 从本地找消息
    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    // 发送是异步进行的
    public static void push(final MsgCreateModel model) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "push: " + model.toString());
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED)
                    return;

                // 我们在发送的时候需要通知界面更新状态，Card;
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);

                if (card.getType() != Message.TYPE_STR) {
                    // 不是文字类型
                    if (!card.getContent().startsWith(UploadHelper.ENDPOINT)) {
                        // 没有上传到云服务器的，还是本地手机文件
                        String content;

                        switch (card.getType()) {
                            case Message.TYPE_PIC:
                                content = uploadPicture(card.getContent());
                                break;
                            case Message.TYPE_AUDIO:
                                content = uploadAudio(card.getContent());
                                break;
                            default:
                                content = "";
                                break;
                        }

                        if (TextUtils.isEmpty(content)) {
                            // 失败
                            card.setStatus(Message.STATUS_FAILED);
                            Factory.getMessageCenter().dispatch(card);
                            // 直接返回
                            return;
                        }


                        // 成功则把网络路径进行替换
                        card.setContent(content);
                        Factory.getMessageCenter().dispatch(card);
                        // 因为卡片的内容改变了，而我们上传到服务器是使用的model，
                        // 所以model也需要跟着更改
                        model.refreshByCard();
                    }
                }

                // 直接发送, 进行网络调度
                RemoteService service = Network.remote();
                Observable<RspModel<MessageCard>> rspModelObservable = service.msgPush(model);
                rspModelObservable.subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .subscribe(new Observer<RspModel<MessageCard>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(RspModel<MessageCard> rspModel) {
                                Log.d(TAG, "onNext: ");
                                if (rspModel != null && rspModel.success()) {
                                    MessageCard rspCard = rspModel.getResult();
                                    if (rspCard != null) {
                                        // 成功的调度
                                        Factory.getMessageCenter().dispatch(rspCard);
                                    }
                                } else {
                                    // 检查是否是账户异常
                                    Factory.decodeRspCode(rspModel);
                                    card.setStatus(Message.STATUS_FAILED);
                                    Factory.getMessageCenter().dispatch(card);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: ");
                                card.setStatus(Message.STATUS_FAILED);
                                Factory.getMessageCenter().dispatch(card);
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
                            }
                        });
            }
        });
    }

    // 上传图片
    private static String uploadPicture(String path) {
        File file = null;
        try {
            // 通过Glide的缓存区间解决了图片外部权限的问题
            file = Glide.with(Factory.app())
                    .load(path)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file != null) {
            // 进行压缩
            String cacheDir = Application.getCacheDirFile().getAbsolutePath();
            String tempFile = String.format("%s/image/Cache_%s.png", cacheDir, SystemClock.uptimeMillis());

            try {
                // 压缩工具类
                if (PicturesCompressor.compressImage(file.getAbsolutePath(), tempFile,
                        Common.Constance.MAX_UPLOAD_IMAGE_LENGTH)) {
                    // 上传
                    String ossPath = UploadHelper.getInstance().uploadImage(tempFile);

                    StreamUtil.delete(path);
                    return ossPath;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    // 上传语音
    private static String uploadAudio(String content) {
        // 上传语音
        File file = new File(content);
        if (!file.exists() || file.length() <= 0)
            return null;
        // 上传并返回
        return UploadHelper.getInstance().uploadAudio(content);
    }
    /**
     * 查询一个消息，这个消息是一个群中的最后一条消息
     *
     * @param groupId 群Id
     * @return 群中聊天的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false) // 倒序查询
                .querySingle();
    }

    /**
     * 查询一个消息，这个消息是和一个人的最后一条聊天消息
     *
     * @param userId UserId
     * @return 聊天的最后一条消息
     */
    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false) // 倒序查询
                .querySingle();
    }
}
