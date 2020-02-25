package com.example.factory.data.message;

import com.example.common.factory.data.DbDataSource;
import com.example.factory.model.db.Message;
/**
 * 消息的数据源定义，他的实现是：MessageRepository
 * 关注的对象是Message表
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
