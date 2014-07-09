package com.guotion.sicilia.bean.net;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:47
 * To change this template use File | Settings | File Templates.
 */
public class LastRead {
    private String _id="";
    /**
     * ChatGroup类型或者String类型
     */
    private List<Object> groups;
    /**
     * User类型，或者String类型
     */
    private Object user;
    private String dict = "";

    public LastRead() {}

    public LastRead(String _id, List<Object> groups, Object user, String dict) {
        this._id = _id;
        this.groups = groups;
        this.user = user;
        this.dict = dict;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Object> getGroups() {
        return groups;
    }

    public void setGroups(List<Object> groups) {
        this.groups = groups;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    @Override
    public String toString() {
        return "LastRead{" +
                "_id='" + _id + '\'' +
                ", groups=" + groups +
                ", user=" + user +
                ", dict='" + dict + '\'' +
                '}';
    }
}
