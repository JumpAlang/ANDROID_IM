package com.example.imapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.common.common.app.PresenterToolbarActivity;
import com.example.common.common.app.ToolbarActivity;
import com.example.common.common.widget.recycler.RecyclerAdapter;
import com.example.common.utils.DateTimeUtil;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.presenter.group.GroupContract;
import com.example.factory.presenter.group.GroupCreateContract;
import com.example.factory.presenter.group.GroupPresenter;
import com.example.factory.presenter.group.GroupsContract;
import com.example.factory.presenter.group.GroupsPresenter;
import com.example.imapp.R;

import butterknife.BindView;

public class GroupActivity extends PresenterToolbarActivity<GroupContract.Presenter>
        implements GroupContract.View {
    public static final String TAG="GroupActivity";
    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String mReceiverId;

    @BindView(R.id.group_name)
    TextView groupName;

    @BindView(R.id.group_number)
    RecyclerView groupNumber;

    @BindView(R.id.join_group_time)
    TextView groupJoinTime;

    @BindView(R.id.group_message)
    TextView groupMessage;

    @BindView(R.id.group_detail)
    TextView groupDetail;

    public static void show(Context context, String groupId) {
        Intent intent = new Intent(context, GroupActivity.class);
        intent.putExtra(BOUND_KEY_ID, groupId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group;
    }
    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(mReceiverId);
    }
    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
    }

    @Override
    protected void initData() {
        super.initData();
        Group fromLocal = GroupHelper.findFromLocal(mReceiverId);
        Log.d(TAG, fromLocal.toString());
        mPresenter.start();
    }

    @Override
    protected GroupContract.Presenter initPresenter() {
        mPresenter=new GroupPresenter(this);
        return mPresenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.refresh();
    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerAdapter() {
        return null;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    @Override
    public String getGroupId() {
        return mReceiverId;
    }

    @Override
    public void onLoadDone(Group group) {
        groupName.setText(group.getName());
        groupDetail.setText(group.getDesc());
        String notifyLevel="接收并提醒";
        switch (group.getNotifyLevel()){
            case Group.NOTIFY_LEVEL_NONE:
                notifyLevel="接收并提醒";
                break;
            case Group.NOTIFY_LEVEL_CLOSE:
                notifyLevel="接收消息不提示";
                break;
            case Group.NOTIFY_LEVEL_INVALID:
                notifyLevel="不接收消息";
                break;

        }
        groupMessage.setText(notifyLevel);
        groupJoinTime.setText(DateTimeUtil.getSampleDate(group.getJoinAt()));
        loadbar(group);
    }
    public void loadbar(Group group){
        Glide.with(this)
                .asDrawable()
                .load(group.getPicture())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Log.d(TAG, "onResourceReady: ");
                        getSupportActionBar().setBackgroundDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.d(TAG, "onLoadCleared: ");
                    }
                });
    }
}
