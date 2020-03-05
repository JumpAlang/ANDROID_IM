package com.example.imapp.frags.message;



import android.util.Log;

import com.example.factory.model.db.User;
import com.example.factory.presenter.message.ChatContract;
import com.example.factory.presenter.message.ChatUserPresenter;

/**
 * 用户聊天界面
 */
public class ChatUserFragment extends ChatFragment<User> implements ChatContract.UserView{
    public static final String TAG="ChatUserFragment";

    public ChatUserFragment() {
        Log.d(TAG, "ChatUserFragment: ");
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        Log.d(TAG, "initPresenter: "+mReceiverId);
        mPresenter=new ChatUserPresenter(this,mReceiverId);
        return mPresenter;
    }
}
