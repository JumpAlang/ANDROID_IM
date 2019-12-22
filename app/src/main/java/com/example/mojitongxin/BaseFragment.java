package com.example.mojitongxin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends androidx.fragment.app.Fragment {
    protected View mRoot;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //初始话参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRoot==null){
            int layId = getContentLayout();
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot=root;
        }else{
            //如果存在需要移除父布局
            if(mRoot.getParent()!=null){
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }
    /**
     * 初始话参数
     * @param bundlele
     * @return
     */
    protected void initArgs(Bundle bundlele){

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
    protected void initWidget(View root){

    }
    /**
     * 初始话数据
     */
    protected void initData(){

    }

    /**
     * f返回键调用
     * @return true已经处理 false 未处理
     */
    public boolean onBackPress(){
        return false;
    }
}
