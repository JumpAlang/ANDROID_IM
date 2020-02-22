package com.example.factory.presenter.group;

import androidx.recyclerview.widget.DiffUtil;

import com.example.factory.Factory;
import com.example.factory.data.group.GroupsDataSource;
import com.example.factory.data.group.GroupsRepository;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 我的群组Presenter
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class GroupsPresenter extends BaseSourcePresenter<Group, Group,
        GroupsDataSource, GroupsContract.View> implements GroupsContract.Presenter {

    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        GroupHelper.refreshGroups();
//        Factory.runOnAsync(new Runnable() {
//            @Override
//            public void run() {
//                GroupsContract.View view = getView();
//                if (view != null) {
//                    String id = view.getGroupId();
//                    Group group = GroupHelper.search(id);
//                    onLoaded(view, group);
//                    //获取数据刷新成员
//                    GroupHelper.refreshGroups();
//                }
//            }
//        });
    }
//    private void onLoaded(final GroupsContract.View view, final Group group){
//        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
//            @Override
//            public void run() {
//                view.onLoadDone(group);
//            }
//        });
//    }
    @Override
    public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view = getView();
        if (view == null)
            return;

        // 对比差异
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 界面刷新
        refreshData(result, groups);
    }
}
