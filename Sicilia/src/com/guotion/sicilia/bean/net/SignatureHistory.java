package com.guotion.sicilia.bean.net;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:36
 * To change this template use File | Settings | File Templates.
 */
public class SignatureHistory {
    public String _id = "";
    public String content = "";//签名内容
    public String date = "";//日期
    public Object user;//所有者

    public SignatureHistory() {
    }

    public SignatureHistory(String _id, String content, String date, Object user) {
        this._id = _id;
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SignatureHistory{" +
                "_id='" + _id + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", user=" + user +
                '}';
    }
}
