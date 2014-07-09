package com.guotion.sicilia.bean.net;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:54
 * To change this template use File | Settings | File Templates.
 */
public class Device {
    private String type = "";
    private int badge;
    private String uid = "";
    private String userName = "";
    /**
     * User类型或者String类型
     */
    private Object user;

    public Device() {}

    public Device(String type, int badge, String uid, String userName, Object user) {
        this.type = type;
        this.badge = badge;
        this.uid = uid;
        this.userName = userName;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }
}
