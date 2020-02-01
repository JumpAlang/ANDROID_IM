package com.example.imapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.common.common.app.Activity;
import com.example.common.common.app.Application;
import com.example.factory.persistence.Account;
import com.example.imapp.LaunchActivity;
import com.example.imapp.R;
import com.example.imapp.frags.main.ActiveFragment;
import com.example.imapp.frags.main.ContactFragment;
import com.example.imapp.frags.main.GroupFragment;
import com.example.imapp.helper.NavHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;

import static com.example.imapp.activities.SearchActivity.TYPE_USER;

public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer>,NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG="MainActivity";

    @BindView(R.id.app_title)
    TextView mTitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottom_menu)
    BottomNavigationView mNavigationBottom;

    @BindView(R.id.left_menu)
    NavigationView mNavigationLeft;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;

    private NavHelper<Integer> mNavHelper;

    /**
     * MainActivity 显示的入口
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if(Account.isComplete()) {
            // 判断用户信息是否完全，完全则走正常流程
            return super.initArgs(bundle);
        }else{
            UserActivity.show(this);
            return true;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        Log.d(TAG, "initWidget");
        super.initWidget();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // 初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container,
                getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
        // 添加对底部与左侧导航栏监听
        mNavigationBottom.setOnNavigationItemSelectedListener(this);
        mNavigationLeft.setNavigationItemSelectedListener(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        // 从底部导中接管我们的Menu，然后进行手动的触发第一次点击
        Menu menu = mNavigationBottom.getMenu();
        // 触发首次选中Home
        menu.performIdentifierAction(R.id.action_home, 0);
    }

    /**
     * 创建标题栏布局
     * @param menu 布局文件
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    /**
     * NavHelper 处理后回调的方法
     *
     * @param newTab 新的Tab
     * @param oldTab 就的Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        // 从额外字段中取出我们的Title资源Id
        mTitle.setText(newTab.extra);
    }
    /**
     * 底部导航被点击事件
     *
     * @param item MenuItem
     * @return True 代表我们能够处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected:"+item);
        // 转接事件流到工具类中
        return mNavHelper.performClickMenu(item.getItemId());
    }
    /**
     * toolbar点击事件
     * @param item 点击的item
     * @return true已经处理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected:"+item);
        switch (item.getItemId()){
            case R.id.search:
//                Account.clear(this);
//                AccountActivity.show(this,true);
                //todo 先默认搜索人
                SearchActivity.show(this,TYPE_USER);
                break;
            case R.id.add:

                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

}
