package com.example.imapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.common.common.app.Activity;
import com.example.imapp.R;
import com.king.zxing.util.CodeUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

public class ZXingCreateActivity extends Activity {
    public static final String GROUPINFO="GROUPINFO";

    String mGroupId;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.QRCode)
    ImageView mQRCode;

    public static void show(GroupActivity context, String groupId) {
        Intent intent = new Intent(context, ZXingCreateActivity.class);
        intent.putExtra(GROUPINFO,groupId);
        context.startActivity(intent);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setSupportActionBar(mToolbar);
        initTitleNeedBack();
    }
    protected void initTitleNeedBack() {
        // 设置左上角的返回按钮为实际的返回效果
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle("签到");
    }
    @Override
    protected boolean initArgs(Bundle bundle) {
        mGroupId = bundle.getString(GROUPINFO);
        return !TextUtils.isEmpty(GROUPINFO);
    }

    @Override
    protected void initData() {
        super.initData();
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
        Bitmap bitmap = CodeUtils.createQRCode(mGroupId, 600,icon);
        mQRCode.setImageBitmap(bitmap);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_zxing_create;
    }



}
