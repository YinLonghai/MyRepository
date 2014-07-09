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
    public String type = "";//cloud and framily
    public List<String> tags;

    public Tag() {}

    public Tag(String type, List<String> tags) {
        this.type = type;
        this.tags = tags;
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
