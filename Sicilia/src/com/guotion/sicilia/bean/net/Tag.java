package com.guotion.sicilia.bean.net;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:51
 * To change this template use File | Settings | File Templates.
 */
public class Tag {
	public String _id;
    public String type = "";//C-cloud and F-framily
    public List<String> tags;

    public Tag() {}

    public Tag(String _id, String type, List<String> tags) {
		super();
		this._id = _id;
		this.type = type;
		this.tags = tags;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
