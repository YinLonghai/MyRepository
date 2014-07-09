package com.guotion.sicilia.bean.net;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:20
 * To change this template use File | Settings | File Templates.
 */
public class CloudItem implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2815685823057954096L;
	public String _id="";
	public String type = "";//img mp3 zip or etc
	public String name = "";
	public String url = "";//server上的地址
    /**
     * User类型或者String类型
     */
	public Object owner;//所有者

    public CloudItem() {}

    public CloudItem(String _id, String type, String name, String url, Object owner) {
        this._id = _id;
        this.type = type;
        this.name = name;
        this.url = url;
        this.owner = owner;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "CloudItem{" +
                "_id='" + _id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", owner=" + owner +
                '}';
    }
}
