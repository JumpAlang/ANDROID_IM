package com.example.imapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.common.common.app.Activity;
import com.example.common.common.app.Fragment;
import com.example.common.common.app.ToolbarActivity;
import com.example.common.factory.model.Author;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Session;
import com.example.imapp.R;
import com.example.imapp.frags.message.ChatGroupFragment;
import com.example.imapp.frags.message.ChatUserFragment;

import butterknife.BindView;

public class MessageActivity extends ToolbarActivity {
    public static final String TAG="MessageActivity";
    // 接收Id，可以是群，也可以是人的Id
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    // 接收名称或者群名称
    public static final String KEY_RECEIVER_NAME="KEY_RECEIVER_NAME";
    // 是否是群
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;
    private String sessionName;

    @BindView(R.id.session_title)
    TextView sessionTitle;
    /**
     * 通过Session发起聊天
     *
     * @param context 上下文
     * @param session Session
     */
    public static void show(Context context, Session session) {
        if (session == null || context == null || TextUtils.isEmpty(session.getId()))
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_NAME,session.getTitle());
        intent.putExtra(KEY_RECEIVER_ID, session.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.person,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected:"+item);
        PersonalActivity.show(this,mReceiverId);
        return true;
    }
    /**
     * 显示人的聊天界面
     *
     * @param context 上下文
     * @param author  人的信息
     */
    public static void show(Context context, Author author) {
        if (author == null || context == null || TextUtils.isEmpty(author.getId()))
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_NAME,author.getName());
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        context.startActivity(intent);
    }

    /**
     * 发起群聊天
     *
     * @param context 上下文
     * @param group   群的Model
     */
    public static void show(Context context, Group group) {
        if (group == null || context == null || TextUtils.isEmpty(group.getId()))
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_NAME,group.getName());
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        sessionName=bundle.getString(KEY_RECEIVER_NAME);
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        Log.d(TAG, "initWidget: "+mIsGroup);
        super.initWidget();
        setTitle("");
        Fragment fragment;
        sessionTitle.setText(sessionName);
        if (mIsGroup){
            fragment = new ChatGroupFragment();
        } else {
            fragment = new ChatUserFragment();
        }

        // 从Activity传递参数到Fragment中去
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.message_fragment, fragment)
                .commit();
    }
}
