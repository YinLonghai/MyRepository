package com.guotion.sicilia.bean.net;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:16
 * To change this template use File | Settings | File Templates.
 */
public class CloudEvent {
    
	public String _id="";
    public String name = "";  //上传的title
    public String desc = "";  //描述
    public String date = ""; //系统生成的日期 格式“2013-21-31 15：36：32”
    public String directory = ""; //保留字段
    public String isPrivate = ""; //是否为私有，1为私有，0为公有
    /**
     * CloudItem类型或者String类型
     */
    public List<Object> files ;//上传的zip中包含的所有文件
    /**
     * Activity类型或者String类型
     */
    public Object activity;
    /**
     * User类型或者String类型
     */
    public Object owner;//所有者

    public CloudEvent(){}

    public CloudEvent(String _id, String name, String desc, String date, String directory,
                      String aPrivate, List<Object> files, Object activity, Object owner) {
        this._id = _id;
        this.name = name;
        this.desc = desc;
        this.date = date;
        this.directory = directory;
        isPrivate = aPrivate;
        this.files = files;
        this.activity = activity;
        this.owner = owner;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getPrivate() {
        return isPrivate;
    }

    public void setPrivate(String aPrivate) {
        isPrivate = aPrivate;
    }

    public List<Object> getFiles() {
        return files;
    }

    public void setFiles(List<Object> files) {
        this.files = files;
    }

    public Object getActivity() {
        return activity;
    }

    public void setActivity(Object activity) {
        this.activity = activity;
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "CloudEvent{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                ", directory='" + directory + '\'' +
                ", isPrivate='" + isPrivate + '\'' +
                ", files=" + files +
                ", activity=" + activity +
                ", owner=" + owner +
                '}';
    }

}
