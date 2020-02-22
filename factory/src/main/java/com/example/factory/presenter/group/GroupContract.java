package com.example.factory.presenter.group;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.view.MemberUserModel;

public interface GroupContract {
    interface Presenter extends BaseContract.Presenter {
        // 具有一个刷新的方法
        void refresh();
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {
        // 获取群的ID
        String getGroupId();

        public void onLoadDone(Group group);
    }
}