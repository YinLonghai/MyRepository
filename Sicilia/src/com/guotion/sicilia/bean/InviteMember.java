package com.guotion.sicilia.bean;

import java.io.Serializable;

/**
 * @function 邀请成员中listview的每一个item
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-7 下午5:16:30
 *
 */
public class InviteMember implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String imgHead;            // 头像    图片在本地，则为本地路径     图片在网络，则为url
	private String accoutName;
	private Boolean isChecked;
	
	public InviteMember() {}

	public InviteMember(String imgHead, String accoutName) {
		this.imgHead = imgHead;
		this.accoutName = accoutName;
	}

	public InviteMember(String imgHead, String accoutName, Boolean isChecked) {
		this.imgHead = imgHead;
		this.accoutName = accoutName;
		this.isChecked = isChecked;
	}

	public String getImgHead() {
		return imgHead;
	}

	public void setImgHead(String imgHead) {
		this.imgHead = imgHead;
	}

	public String getAccoutName() {
		return accoutName;
	}

	public void setAccoutName(String accoutName) {
		this.accoutName = accoutName;
	}

	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	
}
