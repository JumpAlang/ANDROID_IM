package com.example.factory.data.message;


import com.example.factory.model.card.MessageCard;

/**
 * 消息中心，进行消息卡片的消费
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
