package com.example.imapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.common.app.Activity;
import com.example.common.common.app.PresenterToolbarActivity;
import com.example.common.common.widget.PortraitView;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.contact.PersonalContract;
import com.example.factory.presenter.contact.PersonalPresenter;
import com.example.imapp.R;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.factory.model.db.User.SEX_MAN;

public class PersonalActivity extends PresenterToolbarActivity<PersonalContract.Presenter>
        implements PersonalContract.View {
    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String userId;

    @BindView(R.id.ivAvatar)
    ImageView mPortrait;

    @BindView(R.id.group_name)
    TextView mName;

    @BindView(R.id.text_sex)
    TextView sex;

    @BindView(R.id.text_detail)
    TextView mDesc;

    @BindView(R.id.text_phone)
    TextView mPhone;

    @BindView(R.id.btn_say_hello)
    Button mSayHello;

    @BindView(R.id.btn_delete)
    Button mDelete;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        if(userId.equals(Account.getUserId())){
            mSayHello.setVisibility(View.INVISIBLE);
            mDelete.setVisibility(View.INVISIBLE);
        }
        setTitle("详细资料");
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.search, menu);
//        mFollowItem = menu.findItem(R.id.action_follow);
//        changeFollowItemStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_follow) {
//            // TODO 进行关注操作
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_say_hello)
    void onSayHelloClick() {
        // 发起聊天的点击
        User user = mPresenter.getUserPersonal();
        if (user == null)
            return;
        MessageActivity.show(this, user);
    }


    /**
     * 更改关注菜单状态
     */
//    private void changeFollowItemStatus() {
//        if (mFollowItem == null)
//            return;
//
//        // 根据状态设置颜色
//        Drawable drawable = mIsFollowUser ? getResources()
//                .getDrawable(R.drawable.ic_favorite) :
//                getResources().getDrawable(R.drawable.ic_favorite_border);
//        drawable = DrawableCompat.wrap(drawable);
//        DrawableCompat.setTint(drawable, Resource.Color.WHITE);
//        mFollowItem.setIcon(drawable);
//    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void onLoadDone(User user) {
        if (user == null)
            return;
        Glide.with(this)
                .asBitmap()
                .load(user.getPortrait())
                .into(mPortrait);
        mName.setText(user.getName());
        mDesc.setText(user.getDesc());
        mPhone.setText(user.getPhone());
        if(user.getSex()==SEX_MAN){
            sex.setText("男");
        }else {
            sex.setText("女");
        }
        hideLoading();
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
//        mIsFollowUser = isFollow;
//        changeFollowItemStatus();
    }

    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
    }

}
