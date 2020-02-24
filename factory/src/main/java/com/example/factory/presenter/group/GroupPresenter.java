package com.example.factory.presenter.group;

import com.example.common.factory.presenter.BaseRecyclerPresenter;
import com.example.factory.Factory;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.view.MemberUserModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class GroupPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupContract.View>
        implements GroupContract.Presenter {

    public GroupPresenter(GroupContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        // 显示Loading
        start();
        // 异步加载
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                GroupContract.View view = getView();
                if (view != null) {
                    String id = view.getGroupId();
                    Group group = GroupHelper.searchFirstOfNet(id);
                    onLoaded(view,group);
                }
            }
        });
        // 异步加载
        Factory.runOnAsync(loader);
    }
    private void onLoaded(final GroupContract.View view, final Group group){
        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                view.onLoadDone(group);
            }
        });
    }
    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupContract.View view = getView();
            if (view == null)
                return;

            String groupId = view.getGroupId();

            // 传递数量为-1 代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, 5);

            refreshData(models);
        }
    };
}

