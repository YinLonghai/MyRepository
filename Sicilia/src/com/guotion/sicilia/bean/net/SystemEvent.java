package com.guotion.sicilia.bean.net;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:49
 * To change this template use File | Settings | File Templates.
 */
public class SystemEvent {
    private String _id="";
    /**
     * Activity类型或者String类型
     */
    private Object toActivity;
    /**
     * User类型或者String类型
     */
    private Object toSingleUser;
    private String toAdmin = "0"; // 0 or 1
    private String toAll = "0";
    private String eventType = "";
    private String eventID = "";
    private String msgContent = "";

    public SystemEvent() {}

    public SystemEvent(String _id, Object toActivity, Object toSingleUser, String toAdmin,
                       String toAll, String eventType, String eventID, String msgContent) {
        this._id = _id;
        this.toActivity = toActivity;
        this.toSingleUser = toSingleUser;
        this.toAdmin = toAdmin;
        this.toAll = toAll;
        this.eventType = eventType;
        this.eventID = eventID;
        this.msgContent = msgContent;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Object getToActivity() {
        return toActivity;
    }

    public void setToActivity(Object toActivity) {
        this.toActivity = toActivity;
    }

    public Object getToSingleUser() {
        return toSingleUser;
    }

    public void setToSingleUser(Object toSingleUser) {
        this.toSingleUser = toSingleUser;
    }

    public String getToAdmin() {
        return toAdmin;
    }

    public void setToAdmin(String toAdmin) {
        this.toAdmin = toAdmin;
    }

    public String getToAll() {
        return toAll;
    }

    public void setToAll(String toAll) {
        this.toAll = toAll;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    @Override
     public String toString() {
        return "SystemEvent{" +
                "_id='" + _id + '\'' +
                ", toActivity=" + toActivity +
                ", toSingleUser=" + toSingleUser +
                ", toAdmin='" + toAdmin + '\'' +
                ", toAll='" + toAll + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventID='" + eventID + '\'' +
                ", msgContent='" + msgContent + '\'' +
                '}';
    }
}
