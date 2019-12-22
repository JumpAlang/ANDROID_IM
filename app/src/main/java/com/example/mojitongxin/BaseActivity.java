package com.example.mojitongxin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        if(initArgs(getIntent().getExtras())){
            int layId=getContentLayout();
            setContentView(layId);
            initData();
            initWidget();
        }else{
            finish();
        }

    }

    /**
     * 初始话参数
     * @param bundlele
     * @return
     */
    protected boolean initArgs(Bundle bundlele){
        return true;
    }
    /**
     * 返回布局，必须复写
     * @return
     */
    protected  abstract int getContentLayout();
    /**
     * 初始话窗口
     */
    protected void initWindows(){

    }
    /**
     * 初始话控件
     */
    protected void initWidget(){

    }

    /**
     * 初始话数据
     */
    protected void initData(){

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments=getSupportFragmentManager().getFragments();
        if(fragments!=null && fragments.size()>0){
            for(Fragment fragment : fragments){
                if(fragment instanceof BaseFragment){
                    if(((BaseFragment) fragment).onBackPress()){
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
