package com.example.factory.presenter.message;

import android.util.Log;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.message.MessageRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {
    public static final String TAG="ChatUserPresenter";
    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        // 数据源，View，接收者，接收者的类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
        Log.d(TAG, "ChatUserPresenter: ");
    }

    @Override
    public void start() {
        super.start();
        Log.d(TAG, "start: ");
    }

    @Override
    public void pushText(String content) {
        super.pushText(content);
    }

    @Override
    public void onDataLoaded(final List<Message> messages) {
        Log.d(TAG, "onDataLoaded: "+messages.size());
        super.onDataLoaded(messages);
        List<Message> old = getView().getAdapter().getMessageList();
        for (int i = 0; i < old.size(); i++) {
            Log.d(TAG, "onDataLoaded: "+old.get(i).getType());
        }
        //初次进入或者没有消息时
        if(old.size()==0){
            getView().getAdapter().addToEndChronologically(messages);
            return;
        }

        for (int i = 0; i < messages.size(); i++) {
            final Message newMessage = messages.get(i);
            //判断新消息种是否对老消息有所改变
            if(i<old.size()){
                Message oldMessage = old.get(i);
                //如果不相同更新
                if(!oldMessage.isSame(newMessage)){
                    //todo 更新前面的消息
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
}
