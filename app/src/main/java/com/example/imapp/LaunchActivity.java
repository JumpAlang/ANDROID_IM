package com.example.imapp;

import android.Manifest;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.common.common.app.Activity;
import com.example.common.common.app.Application;
import com.example.factory.persistence.Account;
import com.example.imapp.activities.AccountActivity;
import com.example.imapp.activities.MainActivity;
import com.example.imapp.helper.PermissionHelper;
import com.google.android.material.button.MaterialButton;

import net.qiujuer.genius.ui.widget.Loading;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.AutoSizeConfig;
import pub.devrel.easypermissions.EasyPermissions;


public class LaunchActivity extends Activity implements EasyPermissions.PermissionCallbacks{
    public static final String TAG="LaunchActivity";
    private static final int RC = 0x0100;

    @BindView(R.id.register_btn)
    MaterialButton registerBtn;
    @BindView(R.id.login_btn)
    MaterialButton loginBtn;
    @BindView(R.id.loading)
    Loading loading;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //初始话autoSize
        AutoSizeConfig.getInstance()
                .setBaseOnWidth(true)
                .getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportDP(true);
    }

    @Override
    protected void initData() {
        super.initData();
        showLoading();
        waitPushReceiverId();
        if(!PermissionHelper.haveAll(this)){
            EasyPermissions.requestPermissions(this,getResources().getString(R.string.label_permission_denied),RC,PermissionHelper.perms);
        }
    }

    /**
     * 等待个推框架对我们的PushId设置好值
     */
    private void waitPushReceiverId() {
        if (Account.isLogin()) {
            // 如果没有绑定则等待广播接收器进行绑定
            if (Account.isBind()) {
                Log.d(TAG, "登陆，且绑定了ID，直接进入主界面"+Account.isBind());
                MainActivity.show(this);
                return;
            }
        } else {
            // 如果拿到了PushId, 没有登录是不能绑定PushId的
            if (!TextUtils.isEmpty(Account.getPushId())) {
                Log.d(TAG, "没有登陆，获取pushid："+Account.getPushId());
                // 跳转
                showBtn();
                return;
            }
        }
        // 循环等待
        getWindow().getDecorView()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waitPushReceiverId();
                    }
                }, 500);
    }
    @OnClick(R.id.login_btn)
    void onLoginClick(){
        AccountActivity.show(this);
    }
    @OnClick(R.id.register_btn)
    void onRegisterClick(){
        MainActivity.show(this);
    }

    /**
     * 显示加载，等待判断是否登陆，或等待个推分发设备ID
     */
    private void showLoading(){
        loginBtn.setVisibility(View.GONE);
        registerBtn.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    /**
     * 显示登陆，注册按钮
     */
    private void showBtn(){
        loginBtn.setVisibility(View.VISIBLE);
        registerBtn.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted: ");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied: ");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 传递对应的参数，并且告知接收权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
