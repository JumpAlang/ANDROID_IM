package com.example.imapp.frags.message;


import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.common.common.app.PresenterFragment;
import com.example.common.common.widget.ChatView;
import com.example.common.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.Message;
import com.example.factory.presenter.message.ChatContract;
import com.example.imapp.R;
import com.example.imapp.activities.MessageActivity;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import butterknife.BindView;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public abstract class ChatFragment<T> extends PresenterFragment<ChatContract.Presenter>
        implements ChatContract.View<T>, EasyPermissions.PermissionCallbacks, View.OnTouchListener, SensorEventListener, OnMenuClickListener {
    public static final String TAG = "ChatFragment";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;

    @BindView(R.id.chat_view)
    ChatView mChatView;

    @BindView(R.id.msg_list)
    MessageList mMsgList;

    @BindView(R.id.chat_input)
    ChatInputView mChatInput;

    @BindView(R.id.pull_to_refresh_layout)
    PullToRefreshLayout mPtrLayout;

    MsgListAdapter<Message> adapter;
    String mReceiverId;
    String sessionName;
    private InputMethodManager mImm;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }

    public ChatFragment() {
        Log.d(TAG, "ChatFragment: ");
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mChatView.initModule(mMsgList, mChatInput, mPtrLayout);
        mChatView.setOnTouchListener(this);
        mChatView.setMenuClickListener(this);
        //录制声音相关
        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                Log.d(TAG, "onStartRecord: ");
                String path = Environment.getExternalStorageDirectory().getPath() + "/voice";
                File destDir = new File(path);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                mChatView.setRecordVoiceFile(destDir.getPath(), DateFormat.format("yyyy-MM-dd-hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                String path = voiceFile.getPath();
                Log.d(TAG, "onFinishRecord 路径:"+path+" 时常:"+duration);
                mPresenter.pushAudio(path,duration);
            }

            @Override
            public void onCancelRecord() {
                Log.d(TAG, "onCancelRecord: ");
            }

            @Override
            public void onPreviewCancel() {
                Log.d(TAG, "onPreviewCancel: ");
            }

            @Override
            public void onPreviewSend() {
                Log.d(TAG, "onPreviewSend: ");
            }
        });
        //相机相关
        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {

            @Override
            public void onTakePictureCompleted(String photoPath) {
                Log.d(TAG, "onTakePictureCompleted: ");
            }

            @Override
            public void onStartVideoRecord() {
                Log.d(TAG, "onStartVideoRecord: ");
            }

            @Override
            public void onFinishVideoRecord(String videoPath) {
                Log.d(TAG, "onFinishVideoRecord: ");
            }

            @Override
            public void onCancelVideoRecord() {
                Log.d(TAG, "onCancelVideoRecord: ");
            }
        });
        mChatView.getChatInputView().getInputView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onTouch: ");
                scrollToBottom();
                return false;
            }
        });
        mChatView.getSelectAlbumBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Toast.makeText(getActivity(), "OnClick select album button",
                        Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new MsgListAdapter<Message>(sessionName, new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                Glide.with(getActivity())
                        .load(string)
                        .centerCrop()
                        .into(avatarImageView);
            }

            @Override
            public void loadImage(ImageView imageView, String string) {
                Log.d(TAG, "loadImage: "+string);
                Glide.with(ChatFragment.this)
                        .load(string)
                        .into(imageView);
            }

            @Override
            public void loadVideo(ImageView imageCover, String uri) {
                Log.d(TAG, "loadVideo: ");
            }
        });
        adapter.setOnMsgClickListener(message -> {
            Log.d(TAG, "onMessageClick: "+message.getType());
            mPresenter.onclickMessage(message);
        });
        adapter.setMsgStatusViewClickListener(message -> {
            mPresenter.rePush(message);
        });
        mChatView.setAdapter(adapter);
        this.mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
        sessionName = bundle.getString(MessageActivity.KEY_RECEIVER_NAME);
    }

    //发送文件
    @Override
    public void onSendFiles(List<FileItem> mFileItems) {
        Log.d(TAG, "onSendFiles: ");
        for (int i = 0; i < mFileItems.size(); i++) {
            String filePath = mFileItems.get(i).getFilePath();
            Log.d(TAG, "filePath: "+filePath);
        }
        mPresenter.pushImages(mFileItems);
    }

    //单击语音时触发
    @Override
    public boolean switchToMicrophoneMode() {
        Log.d(TAG, "switchToMicrophoneMode: ");
        scrollToBottom();
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (!EasyPermissions.hasPermissions(getActivity(), perms)) {
            EasyPermissions.requestPermissions(getActivity(),
                    getResources().getString(R.string.rationale_record_voice),
                    RC_RECORD_VOICE, perms);
        }
        return true;
    }

    //单击图片时候触发
    @Override
    public boolean switchToGalleryMode() {
        Log.d(TAG, "switchToGalleryMode: ");
        scrollToBottom();
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if (!EasyPermissions.hasPermissions(getActivity(), perms)) {
            EasyPermissions.requestPermissions(getActivity(),
                    getResources().getString(R.string.rationale_photo),
                    RC_PHOTO, perms);
        }
        // If you call updateData, select photo view will try to update data(Last update over 30 seconds.)
        mChatView.getChatInputView().getSelectPhotoView().updateData();
        return true;
    }

    //单击相机时触发
    @Override
    public boolean switchToCameraMode() {
        Log.d(TAG, "switchToCameraMode: ");
        scrollToBottom();
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };

        if (!EasyPermissions.hasPermissions(getActivity(), perms)) {
            EasyPermissions.requestPermissions(getActivity(),
                    getResources().getString(R.string.rationale_camera),
                    RC_CAMERA, perms);
            return false;
        } else {
//            File rootDir = getFilesDir();
//            String fileDir = rootDir.getAbsolutePath() + "/photo";
//            mChatView.setCameraCaptureFile(fileDir, new SimpleDateFormat("yyyy-MM-dd-hhmmss",
//                    Locale.getDefault()).format(new Date()));
        }
        return true;
    }

    //点击表情
    @Override
    public boolean switchToEmojiMode() {
        Log.d(TAG, "switchToEmojiMode: ");
        scrollToBottom();
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onFirstInit() {
        Log.d(TAG, "onFirstInit: ");
        super.onFirstInit();
        // 进行一次数据加载
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    //发送消息
    @Override
    public boolean onSendTextMessage(CharSequence input) {
        String str = input.toString();
        if (str != null && !str.equals("")) {
            Log.d(TAG, "onSendTextMessage: " + input.toString());
            mPresenter.pushText(input.toString());
            return true;
        }
        return false;
    }
    //作废
    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return null;
    }

    @Override
    public MsgListAdapter<Message> getAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    /**
     * 权限申请成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * 权限申请失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ChatInputView chatInputView = mChatView.getChatInputView();
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                mChatView.setMsgListHeight(true);
                try {
                    View v = getActivity().getCurrentFocus();
                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        view.clearFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
        }
        return false;
    }

    private void scrollToBottom() {
        mChatView.setMsgListHeight(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mChatView.getMessageListView().smoothScrollToPosition(0);
            }
        }, 200);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
