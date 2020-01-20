package com.example.imapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.common.common.app.Activity;
import com.example.imapp.R;
import com.example.imapp.frags.account.AccountTrigger;
import com.example.imapp.frags.account.LoginFragment;
import com.example.imapp.frags.account.RegisterFragment;


import butterknife.BindView;

public class AccountActivity extends Activity implements AccountTrigger {
    private static boolean isLogin =true;
    private Fragment mCurFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;

    @BindView(R.id.im_bg)
    ImageView mBg;

    /**
     * 显示入口
     * @param context Context
     */
    public static void show(Context context,boolean isLogin) {
        context.startActivity(new Intent(context, AccountActivity.class));
        AccountActivity.isLogin=isLogin;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        if(isLogin==true){
            mCurFragment = mLoginFragment = new LoginFragment();
        } else {
            mCurFragment = mLoginFragment = new RegisterFragment();
        }
        // 初始化Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                //默认情况下为null，
                //第一次之后就不为null了
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        } else {
            // 因为默认请求下mLoginFragment已经赋值，无须判断null
            fragment = mLoginFragment;
        }

        // 重新赋值当前正在显示的Fragment
        mCurFragment = fragment;
        // 切换显示ø
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lay_container, fragment)
                .commit();
    }
}
