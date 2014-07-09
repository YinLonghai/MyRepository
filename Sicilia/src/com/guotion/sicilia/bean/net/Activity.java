package com.guotion.sicilia.bean.net;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:19
 * To change this template use File | Settings | File Templates.
 */
public class Activity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3336509002086627998L;
	public String _id="";
    public String type = "N";// N 普通活动，B 生日事件
    public String name = "";
    public String content = "";
    public String lunarDate = "";//农历生日
    public String date = "";
    public String location = "";
    public String endDate = "";
    public String remind = ""; //提醒的方式，如提前30分钟，15分钟之类的
    /**
     * CloudEvent 类型,或者String类型
     */
    public List<Object> cloudFiles;
    /**
     * User类型或者String类型
     */
    public Object creator;
    /**
     * User类型，或者String类型
     */
    public List<Object> members;
    public String dict = "";//参与情况 　用户  id:1｜id:0
    public String isTop = "";

    public Activity() {
    }

    public Activity(String _id, String type, String name, String content, String lunarDate, String date, String location, String endDate, String remind,
                    List<Object> cloudFiles, Object creator, List<Object> members, String dict, String top) {
        this._id = _id;
        this.type = type;
        this.name = name;
        this.content = content;
        this.lunarDate = lunarDate;
        this.date = date;
        this.location = location;
        this.endDate = endDate;
        this.remind = remind;
        this.cloudFiles = cloudFiles;
        this.creator = creator;
        this.members = members;
        this.dict = dict;
        isTop = top;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLunarDate() {
        return lunarDate;
    }

    public void setLunarDate(String lunarDate) {
        this.lunarDate = lunarDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public List<Object> getCloudFiles() {
        return cloudFiles;
    }

    public void setCloudFiles(List<Object> cloudFiles) {
        this.cloudFiles = cloudFiles;
    }

    public Object getCreator() {
        return creator;
    }

    public void setCreator(Object creator) {
        this.creator = creator;
    }

    public List<Object> getMembers() {
        return members;
    }

    public void setMembers(List<Object> members) {
        this.members = members;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public String getTop() {
        return isTop;
    }

    public void setTop(String top) {
        isTop = top;
    }
}
