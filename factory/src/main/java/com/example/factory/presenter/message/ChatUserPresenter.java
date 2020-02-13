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

}
