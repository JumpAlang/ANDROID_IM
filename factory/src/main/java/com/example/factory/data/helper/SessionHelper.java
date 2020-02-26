package com.example.factory.data.helper;

import android.util.Log;

import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

/**
 * 会话辅助工具类
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class SessionHelper {
    // 从本地查询Session
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }

    /**
     * 设置当前的Session
     * @param
     * @param isNow
     * @return
     */
    public static Session setNowSession(String id,Boolean isNow){
        ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
        Session sessionLocal = findFromLocal(id);
        if (sessionLocal == null) {
            // 第一次聊天，创建一个你和对方的一个会话
            sessionLocal=new Session();
            sessionLocal.setId(id);
        }
        sessionLocal.setNowSession(isNow);
        adapter.save(sessionLocal);
        return sessionLocal;
    }
}
