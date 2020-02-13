package com.example.imapp.frags.message;

import android.util.Log;

import com.example.factory.data.message.MessageDataSource;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.User;
import com.example.factory.presenter.message.ChatContract;
import com.example.factory.presenter.message.ChatGroupPresenter;
import com.example.factory.presenter.message.ChatPresenter;
import com.example.factory.presenter.message.ChatUserPresenter;

public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView{
    public static final String TAG="ChatUserFragment";

    public ChatGroupFragment() {
        Log.d(TAG, "ChatUserFragment: ");
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        Log.d(TAG, "initPresenter: "+mReceiverId);
        mPresenter=new ChatGroupPresenter(this,mReceiverId);
        return mPresenter;
    }
}
