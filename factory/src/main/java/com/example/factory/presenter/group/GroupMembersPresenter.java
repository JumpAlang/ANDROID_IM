package com.example.factory.presenter.group;

import android.util.Log;

import com.example.common.factory.presenter.BaseRecyclerPresenter;
import com.example.factory.Factory;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.view.MemberUserModel;

import java.util.List;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMembersContract.View>
        implements GroupMembersContract.Presenter {
    public static final String TAG="GroupMembersPresenter";

    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        // 显示Loading
        start();

        // 异步加载
        Factory.runOnAsync(loader);
    }


    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null)
                return;

            String groupId = view.getGroupId();
            // 传递数量为-1 代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);
            for (int i = 0; i < models.size(); i++) {
                Log.d(TAG, "run: "+models.get(i).getName());
            }
            refreshData(models);
        }
    };
}
