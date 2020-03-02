package com.example.factory.presenter.group;

import com.example.common.factory.presenter.BaseRecyclerPresenter;
import com.example.factory.Factory;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.User;
import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.persistence.Account;

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
                    int number=(int)GroupHelper.getMemberCount(id);
                    onLoaded(view,group,number);
                }
            }
        });
        // 异步加载
        Factory.runOnAsync(loader);
    }
    private void onLoaded(final GroupContract.View view, final Group group,final int number){
        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                view.onLoadDone(group,number);
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
    @Override
    public Boolean isAdmin(String mReceiverId) {
        Group group = GroupHelper.findFromLocal(mReceiverId);
        if(group!=null){
            User owner = group.getOwner();
            User user = Account.getUser();
            if(owner.isSame(user))
                return true;
        }
        return false;
    }
}

