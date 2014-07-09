package com.guotion.sicilia.bean.net;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:52
 * To change this template use File | Settings | File Templates.
 */
public class ANotification implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4888786676050764862L;
	public String _id="";
    public String singleton = "note";//单例的id
    public String content = "公告";//公告内容
    public String date = "";//发布公告的时间•
    /**
     * User类型，或者String类型
     */
    public Object editBy; //编辑者

    public ANotification() {}

    public ANotification(String _id, String singleton, String content, String date, Object editBy) {
        this._id = _id;
        this.singleton = singleton;
        this.content = content;
        this.date = date;
        this.editBy = editBy;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSingleton() {
        return singleton;
    }

    public void setSingleton(String singleton) {
        this.singleton = singleton;
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

    public Object getEditBy() {
        return editBy;
    }

    public void setEditBy(Object editBy) {
        this.editBy = editBy;
    }
}
