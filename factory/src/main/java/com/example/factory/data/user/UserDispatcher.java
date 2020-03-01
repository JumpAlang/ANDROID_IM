package com.example.factory.data.user;

import android.text.TextUtils;
import android.util.Log;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class UserDispatcher implements UserCenter {
    private static UserCenter instance;
    // 单线程池；处理卡片一个个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static UserCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null)
                    instance = new UserDispatcher();
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0)
            return;

        // 丢到单线程池中
        executor.execute(new UserCardHandler(cards));
    }

    /**
     * 线程调度的时候会触发run方法
     */
    private class UserCardHandler implements Runnable {
        private final UserCard[] cards;

        UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            // 单被线程调度的时候触发
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                // 进行过滤操作
                if (card == null || TextUtils.isEmpty(card.getId()))
                    continue;
                // 添加操作
                //全部值为关注
                users.add(card.build());
            }

            //要么全是添加 要么全是删除操作
            if(users.get(0).isFollow()){
                Log.d("UserDispatcher", "save");
                DbHelper.save(User.class, users.toArray(new User[0]));
            } else {
                Log.d("UserDispatcher", "delete");
                DbHelper.delete(User.class, users.toArray(new User[0]));
            }
    }
    }
}
