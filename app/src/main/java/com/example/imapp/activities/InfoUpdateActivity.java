package com.example.imapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.common.common.app.ToolbarActivity;
import com.example.factory.presenter.user.UpdateInfoContract;
import com.example.factory.presenter.user.UpdateInfoPresenter;
import com.example.imapp.R;

import androidx.annotation.NonNull;
import butterknife.BindView;

public class InfoUpdateActivity extends ToolbarActivity implements UpdateInfoContract.View{
    public static final String TAG="InfoUpdateActivity";
    public static final String TYPE_CHANGE="TYPE_CHANGE";
    public static final int NAME_CHANGE=1;
    public static final int DETAIL_CHANGE=2;

    @BindView(R.id.text)
    EditText mEditText;

    private int typeChange;

    UpdateInfoContract.Presenter mUpdateInfoPresenter;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, InfoUpdateActivity.class);
        intent.putExtra(TYPE_CHANGE, userId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_update;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        typeChange = bundle.getInt(TYPE_CHANGE,-1);
        return !(typeChange==-1);
    }

    @Override
    protected void initData() {
        super.initData();
        new UpdateInfoPresenter(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle(typeChange);
    }

    @Override
    public void updateSucceed() {
        this.finish();
    }

    @Override
    public void showError(int str) {

    }

    @Override
    public void showLoading() {

    }
    /**
     * 创建标题栏布局
     * @param menu 布局文件
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        if(item.getItemId()==R.id.action_submit){
            String s = mEditText.getText().toString();
            mUpdateInfoPresenter.update(s,typeChange);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(UpdateInfoContract.Presenter presenter) {
        mUpdateInfoPresenter=presenter;
    }
}
