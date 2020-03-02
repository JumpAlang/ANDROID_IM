package com.example.imapp.activities;

import android.content.Context;
import android.content.Intent;

import com.example.common.common.app.Fragment;
import com.example.common.common.app.ToolbarActivity;
import com.example.imapp.R;
import com.example.imapp.frags.user.UpdateInfoFragment;

/**
 * 用户信息界面
 * 可以提供用户信息修改
 */
public class UserActivity extends ToolbarActivity {
    private Fragment mCurFragment;

    /**
     * 显示界面的入口方法
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, UserActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    // Activity中收到剪切图片成功的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }
}
