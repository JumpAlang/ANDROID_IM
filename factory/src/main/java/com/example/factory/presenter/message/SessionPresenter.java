package com.example.factory.presenter.message;

import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.data.message.SessionDataSource;
import com.example.factory.data.message.SessionRepository;
import com.example.factory.model.db.Session;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 最近聊天列表的Presenter
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session,
        SessionDataSource, SessionContract.View> implements SessionContract.Presenter {
    public static final String TAG="SessionPresenter";

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
        Log.d(TAG, "SessionPresenter: ");
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        Log.d(TAG, "onDataLoaded: "+sessions.size());
        SessionContract.View view = getView();
        if (view == null)
            return;
        Collections.sort(sessions, new Comparator<Session>() {
            @Override
            public int compare(Session o1, Session o2) {
                return o1.getModifyAt().compareTo(o2.getModifyAt());
            }
        });
        // 差异对比
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 刷新界面
        refreshData(result, sessions);
    }


    @Override
    public void clearUnReadCount(Session session) {
        DbHelper.instance.clearUnReadCount(session);
    }
}
