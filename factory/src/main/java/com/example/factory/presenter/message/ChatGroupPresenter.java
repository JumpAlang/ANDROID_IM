package com.example.factory.presenter.message;

import android.util.Log;

import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.message.MessageGroupRepository;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.persistence.Account;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView>
        implements ChatContract.Presenter {
    public static final String TAG="ChatGroupPresenter";
    public ChatGroupPresenter(ChatContract.GroupView view, String receiverId) {
        // 数据源，View，接收者，接收者的类型
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();
        Log.d(TAG, "start: ");
        // 拿群的信息
//        Group group = GroupHelper.findFromLocal(mReceiverId);
//        if (group != null) {
//            // 初始化操作
//            ChatContract.GroupView view = getView();
//
//            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
//            view.showAdminOption(isAdmin);
//
//            // 基础信息初始化
//            view.onInit(group);

            // 成员初始化
//            List<MemberUserModel> models = group.getLatelyGroupMembers();
//            final long memberCount = group.getGroupMemberCount();
            // 没有显示的成员的数量
//            long moreCount = memberCount - models.size();
//            view.onInitGroupMembers(models, moreCount);
        }
}
