package com.example.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 数据库的基本信息
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 4;
}
