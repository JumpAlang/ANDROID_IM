package com.example.factory.model.db;

import android.util.Log;

import com.example.factory.persistence.Account;
import com.example.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;


/**
 * 本地的消息表
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
@Table(database = AppDatabase.class)
public class Message extends BaseDbModel<Message> implements  Serializable, IMessage {
    public static final String TAG="Message";
    // 接收者类型
    public static final int RECEIVER_TYPE_NONE = 1;
    public static final int RECEIVER_TYPE_GROUP = 2;

    // 消息类型
    public static final int TYPE_STR = 1;
    public static final int TYPE_PIC = 2;
    public static final int TYPE_FILE = 3;
    public static final int TYPE_AUDIO = 4;

    // 消息状态
    public static final int STATUS_DONE = 0; // 正常状态
    public static final int STATUS_CREATED = 1; // 创建状态
    public static final int STATUS_FAILED = 2; // 发送失败状态

    @PrimaryKey
    private String id;//主键
    @Column
    private String content;// 内容
    @Column
    private String attach;// 附属信息
    @Column
    private int oldType;// 消息类型
    @Column
    private Date createAt;// 创建时间
    @Column
    private int status;// 当前消息的状态

    @ForeignKey(tableClass = Group.class, stubbedRelationship = true)
    private Group group;// 接收者群外键

    @ForeignKey(tableClass = User.class)
    private User sender;// 发送者 外键

    @ForeignKey(tableClass = User.class, stubbedRelationship = true)
    private User receiver;// 接收者人外键

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    @Override
    public String getMsgId() {
        return id;
    }

    public int getOldType() {
        return oldType;
    }

    public void setOldType(int oldType) {
        this.oldType = oldType;
    }

    @Override
    public IUser getFromUser() {
        Log.d(TAG, "getFromUser: "+sender.getAvatarFilePath());
        return sender;
    }

    @Override
    public String getTimeString() {
//        return createAt.toString();
        return null;
    }

    /**
     * 判断当前的消息是发送还是接收
     */
    public boolean isSend(){
        String snederID = sender.getId();
        String myId = Account.getUserId();
        Log.d(TAG, "snederID: "+ snederID);
        return snederID.equals(myId);
    }

    public int getType() {
        MessageType Itype=MessageType.EVENT;
        Log.d(TAG, "onDataLoaded11: "+oldType);
        switch (oldType){
            case TYPE_STR:
                if(isSend())
                    Itype = MessageType.SEND_TEXT;
                else
                    Itype = MessageType.RECEIVE_TEXT;
                break;
            case TYPE_PIC:
                if(isSend())
                    Itype=MessageType.SEND_IMAGE;
                else
                    Itype = MessageType.RECEIVE_IMAGE;
                break;
            case TYPE_AUDIO:
                if(isSend())
                    Itype=MessageType.SEND_VOICE;
                else
                    Itype = MessageType.RECEIVE_VOICE;
                break;
            case TYPE_FILE:
                if(isSend())
                    Itype=MessageType.SEND_FILE;
                else
                    Itype = MessageType.RECEIVE_FILE;
                break;
        }
        return Itype.ordinal();
    }

    @Override
    public MessageStatus getMessageStatus() {
        MessageStatus messageStatus=MessageStatus.RECEIVE_SUCCEED;
        switch (status){
            case STATUS_DONE:
                if(isSend())
                    messageStatus = MessageStatus.SEND_SUCCEED;
                break;
            case STATUS_CREATED:
                if(isSend())
                    messageStatus=MessageStatus.SEND_GOING;
                break;
            case STATUS_FAILED:
                if(isSend())
                    messageStatus=MessageStatus.SEND_FAILED;
                break;
        }
        return messageStatus;
    }

    @Override
    public String getText() {
        return content;
    }

    @Override
    public String getMediaFilePath() {
        return content;
        //todo 媒体储存路径？？
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public String getProgress() {
        return null;
    }

    @Override
    public HashMap<String, String> getExtras() {
        return null;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * 当消息类型为普通消息（发送给人的消息）
     * 该方法用于返回，和我聊天的人是谁
     * <p>
     * 和我聊天，要么对方是发送者，要么对方是接收者
     *
     * @return 和我聊天的人
     */
    User getOther() {
        if (Account.getUserId().equals(sender.getId())) {
            return receiver;
        } else {
            return sender;
        }
    }

    /**
     * 构建一个简单的消息描述
     * 用于简化消息显示
     *
     * @return 一个消息描述
     */
    String getSampleContent() {
        if (oldType == TYPE_PIC)
            return "[图片]";
        else if (oldType == TYPE_AUDIO)
            return "🎵";
        else if (oldType == TYPE_FILE)
            return "📃";
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return oldType == message.oldType
                && status == message.status
                && Objects.equals(id, message.id)
                && Objects.equals(content, message.content)
                && Objects.equals(attach, message.attach)
                && Objects.equals(createAt, message.createAt)
                && Objects.equals(group, message.group)
                && Objects.equals(sender, message.sender)
                && Objects.equals(receiver, message.receiver);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isSame(Message oldT) {
        // 两个类，是否指向的是同一个消息
        return Objects.equals(id, oldT.id);
    }

    @Override
    public boolean isUiContentSame(Message oldT) {
        // 对于同一个消息当中的字段是否有不同
        // 这里，对于消息，本身消息不可进行修改；只能添加删除
        // 唯一会变化的就是本地（手机端）消息的状态会改变
        return oldT == this || status == oldT.status;
    }
}
