package com.example.factory.presenter.message;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

import java.util.List;

import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.messages.MsgListAdapter;

/**
 * 聊天契约
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public interface ChatContract {
    interface Presenter extends BaseContract.Presenter {
        // 发送文字
        void pushText(String content);

        // 发送语音
        void pushAudio(String path,long time);

        // 发送图片
        void pushImages(List<FileItem> mFileItems);
        public void pushImages(String Filepath);
        void onclickMessage(Message message);

        // 重新发送一个消息，返回是否调度成功
        boolean rePush(Message message);
    }

    // 界面的基类
    interface View<T> extends BaseContract.RecyclerView<Presenter, Message> {
        MsgListAdapter<Message> getAdapter();
    }

    // 人聊天的界面
    interface UserView extends View<User> {

    }

    // 群聊天的界面
    interface GroupView extends View<Group> {

    }
}
