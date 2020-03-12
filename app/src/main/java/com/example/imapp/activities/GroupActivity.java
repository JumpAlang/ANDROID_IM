package com.example.imapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.common.app.Application;
import com.example.common.common.app.PresenterToolbarActivity;
import com.example.common.common.widget.PortraitView;
import com.example.common.common.widget.recycler.RecyclerAdapter;
import com.example.common.utils.DateTimeUtil;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.presenter.group.GroupContract;
import com.example.factory.presenter.group.GroupPresenter;
import com.example.imapp.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

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

    @BindView(R.id.more_number)
    RelativeLayout moreNumber;

    @BindView(R.id.group_detail)
    TextView groupDetail;

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @BindView(R.id.group_master)
    TextView groupMaster;

    @BindView(R.id.number)
    TextView groupNumbertext;

    @BindView(R.id.Group_signIn)
    RelativeLayout GroupSignIn;

    RecyclerAdapter<MemberUserModel> mRecycler;

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
        setTitle("详细资料");
        // 初始化Recycler
        groupNumber.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        groupNumber.setAdapter(mRecycler=new RecyclerAdapter<MemberUserModel>() {
            @Override
            protected int getItemViewType(int position, MemberUserModel userCard) {
                // 返回cell的布局id
                return R.layout.cell_portrait_list;
            }

            @Override
            protected ViewHolder<MemberUserModel> onCreateViewHolder(View root, int viewType) {
                return new GroupActivity.ViewHolder(root);
            }
        });
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
        return mRecycler;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    @Override
    public String getGroupId() {
        return mReceiverId;
    }

    @Override
    public void onLoadDone(Group group,int number) {
        groupName.setText(group.getName());
        groupDetail.setText(group.getDesc());
        groupJoinTime.setText(DateTimeUtil.getSampleDate(group.getJoinAt()));
        groupMaster.setText(group.getOwner().getName());
        groupNumbertext.setText(number+"人");
        loadbar(group);
    }
    public void loadbar(Group group){
        Glide.with(this)
                .asDrawable()
                .load(group.getPicture())
                .into(ivAvatar);
    }
    class ViewHolder extends RecyclerAdapter.ViewHolder<MemberUserModel>{

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(MemberUserModel memberUserModel) {
            mPortraitView.setup(Glide.with(GroupActivity.this), memberUserModel.getPortrait());
        }
    }
    @OnClick(R.id.Group_signIn)
    public void groupSignIn(){
        if(mPresenter.isAdmin(mReceiverId)){
        }else {
            Application.showToast("您不是管理员，不能发起签到");
        }
    }

    @OnClick(R.id.more_number)
    public void moreNumber(){
        if(mPresenter.isAdmin(mReceiverId)){
            GroupMemberActivity.showAdmin(this,mReceiverId);
        }else {
            GroupMemberActivity.show(this,mReceiverId);
        }
    }
}
