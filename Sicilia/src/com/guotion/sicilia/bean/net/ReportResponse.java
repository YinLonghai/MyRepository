package com.guotion.sicilia.bean.net;

import com.google.gson.Gson;

public class ReportResponse {
	/**
	 * 管理员id
	 */
	public String usr;
	/**
	 * 举报者和管理员的p2p群组id
	 */
	public String channel;
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getString(){
		return new Gson().toJson(this);
	}
	public String getUsr() {
		return usr;
	}
	public void setUsr(String usr) {
		this.usr = usr;
	}
	
}
