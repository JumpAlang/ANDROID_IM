package com.example.imapp.frags.message;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.common.common.app.Application;
import com.example.common.common.app.Fragment;
import com.example.common.common.app.PresenterFragment;
import com.example.common.common.widget.ChatView;
import com.example.common.common.widget.recycler.RecyclerAdapter;
import com.example.factory.Factory;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.presenter.message.ChatContract;
import com.example.factory.presenter.message.ChatPresenter;
import com.example.factory.presenter.message.ChatUserPresenter;
import com.example.imapp.R;
import com.example.imapp.activities.MessageActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import pub.devrel.easypermissions.EasyPermissions;

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
