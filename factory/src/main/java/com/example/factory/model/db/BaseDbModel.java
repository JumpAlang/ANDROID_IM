package com.example.factory.model.db;

import com.example.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.structure.BaseModel;


/**
 * 我们APP中的基础的一个BaseDbModel，
 * 基础了数据库框架DbFlow中的基础类
 * 同时定义类我们需要的方法
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiffer<Model> {
}
