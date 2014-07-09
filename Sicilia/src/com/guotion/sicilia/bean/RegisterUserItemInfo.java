package com.guotion.sicilia.bean;

import java.io.Serializable;

/**
 * @function 注册管理界面中listview的每一个item
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-7 下午5:16:30
 *
 */
public class RegisterUserItemInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String imgHead;            // 头像    图片在本地，则为本地路径     图片在网络，则为url
	private String accoutName;
	
	public RegisterUserItemInfo() {}

	public RegisterUserItemInfo(String imgHead, String accoutName) {
		this.imgHead = imgHead;
		this.accoutName = accoutName;
	}

	public String getImgHead() {
		return imgHead;
	}

	public void setImg_head(String imgHead) {
		this.imgHead = imgHead;
	}

	public String getAccoutName() {
		return accoutName;
	}

	public void setAccoutName(String accoutName) {
		this.accoutName = accoutName;
	}
	
}
