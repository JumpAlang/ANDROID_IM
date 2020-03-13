package com.example.factory.presenter.message;


import android.text.TextUtils;
import android.util.Log;

import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.db.Message;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BaseSourcePresenter;
import com.github.binarywang.java.emoji.EmojiConverter;

import java.util.List;

import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.commons.models.IMessage;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 聊天Presenter的基础类
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
@SuppressWarnings("WeakerAccess")
public class ChatPresenter<View extends ChatContract.View>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.Presenter {
    public static final String TAG="ChatPresenter";
    // 接收者Id，可能是群，或者人的ID
    protected String mReceiverId;
    // 区分是人还是群Id
    protected int mReceiverType;


    public ChatPresenter(MessageDataSource source, View view,
                         String receiverId, int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }
    @Override
    public void pushText(String content) {
        content= EmojiConverter.getInstance().toAlias(content);
        // 构建一个新的消息
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(content, Message.TYPE_STR)
                .build();
        // 进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void pushAudio(String path,long time) {
        if(TextUtils.isEmpty(path)){
            return;
        }
        // 构建一个新的消息
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(path, Message.TYPE_AUDIO)
                .attach(String.valueOf(time))
                .build();

        // 进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void pushImages(List<FileItem> mFileItems) {
        if (mFileItems == null || mFileItems.size() == 0)
            return;
        // 此时路径是本地的手机上的路径
        for (FileItem mFileItem : mFileItems) {
            // 构建一个新的消息
            MsgCreateModel model = new MsgCreateModel.Builder()
                    .receiver(mReceiverId, mReceiverType)
                    .content(mFileItem.getFilePath(), Message.TYPE_PIC)
                    .build();

            // 进行网络发送
            MessageHelper.push(model);
        }
    }

    @Override
    public void pushImages(String Filepath) {
        if (Filepath == null || Filepath.isEmpty())
            return;
        // 此时路径是本地的手机上的路径
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(Filepath, Message.TYPE_PIC)
                .build();

        // 进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void onclickMessage(Message message) {
        if (message.getType() == IMessage.MessageType.RECEIVE_IMAGE.ordinal()
                || message.getType() == IMessage.MessageType.SEND_IMAGE.ordinal()) {
            //todo 图片显示大图
//                    Intent intent = new Intent(getActivity(), BrowserImageActivity.class);
//                    intent.putExtra("msgId", message.getMsgId());
//                    intent.putStringArrayListExtra("pathList", mPathList);
//                    intent.putStringArrayListExtra("idList", mMsgIdList);
//                    startActivity(intent);
        } else if(message.getType() ==IMessage.MessageType.RECEIVE_VOICE.ordinal()
                ||message.getType()==IMessage.MessageType.SEND_VOICE.ordinal()){
            // 下载工具类
//             FileCache<VoiceViewHolder> mAudioFileCache = new FileCache<>("audio/cache", "mp3",
//                     new FileCache.CacheListener<VoiceViewHolder>() {
//                 @Override
//                 public void onDownloadSucceed(VoiceViewHolder message, File file) {
//                     message.playVoice();
//                 }
//
//                 @Override
//                 public void onDownloadFailed(VoiceViewHolder message) {
//
//                 }
//             });
//             mAudioFileCache.download();
        }
    }

    @Override
    public boolean rePush(Message message) {
        // 确定消息是可重复发送的
        if (Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED) {

            // 更改状态
            message.setStatus(Message.STATUS_CREATED);
            // 构建发送Model
            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }

    @Override
    public void onDataLoaded(final List<Message> messages) {
        Log.d(TAG, "onDataLoaded: "+messages.size());
        final List<Message> old = getView().getAdapter().getMessageList();
        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                //初次进入或者没有消息时
                if(old.size()==0){
                    getView().getAdapter().addToEndChronologically(messages);
                }else {
                    for (int i = 0; i < messages.size(); i++) {
                        final Message newMessage = messages.get(i);
                        if(i<old.size()){
                            Message oldMessage = old.get(old.size()-i-1);
                            if(!oldMessage.isUiContentSame(newMessage)){
                                getView().getAdapter().updateMessage(newMessage);
                            }
                        }else {
                            getView().getAdapter().addToStart(newMessage,true);
                        }
                    }
                }
                //跳转到最下面
                if(messages.size()!=0)
                    getView().getAdapter().getLayoutManager().scrollToPosition(0);
            }
        });
    }
}
