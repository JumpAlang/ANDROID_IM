package com.example.factory.presenter.message;


import android.util.Log;

import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.db.Message;
import com.example.factory.presenter.BaseSourcePresenter;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 聊天Presenter的基础类
 *
 * @author qiujuer Email:qiujuer@live.cn
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
        // 构建一个新的消息
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(content, Message.TYPE_STR)
                .build();

        // 进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void pushAudio(String path) {
        // TODO 发送语音
    }

    @Override
    public void pushImages(String[] paths) {
        // TODO 发送图片
    }

    @Override
    public boolean rePush(Message message) {
        // 确定消息是可重复发送的
//        if (Account.getUserId().equalsIgnoreCase(message.getSender().getId())
//                && message.getStatus() == Message.STATUS_FAILED) {
//
//            // 更改状态
//            message.setStatus(Message.STATUS_CREATED);
//            // 构建发送Model
//            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
//            MessageHelper.push(model);
//            return true;
//        }

        return false;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        Log.d(TAG, "onDataLoaded: "+messages.size());
        List<Message> old = getView().getAdapter().getMessageList();
        for (int i = 0; i < old.size(); i++) {
            Log.d(TAG, "onDataLoaded: "+old.get(i).getType());
        }
        //初次进入或者没有消息时
        if(old.size()==0){
            getView().getAdapter().addToEndChronologically(messages);
        }else {
            for (int i = 0; i < messages.size(); i++) {
                final Message newMessage = messages.get(i);
                //判断新消息种是否对老消息有所改变
                if(i<old.size()){
                    Message oldMessage = old.get(i);
                    //如果不相同更新
                    if(!oldMessage.isSame(newMessage)){
                        getView().getAdapter().updateMessage(newMessage);
                    }
                }else {
                    Log.d(TAG, "messages: "+newMessage.getContent());
                    AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                        @Override
                        public void run() {
                            getView().getAdapter().addToStart(newMessage,true);
                        }
                    });
                }
            }
        }
        //跳转到最下面
        if(messages.size()!=0)
            getView().getAdapter().getLayoutManager().scrollToPosition(0);
    }
}
