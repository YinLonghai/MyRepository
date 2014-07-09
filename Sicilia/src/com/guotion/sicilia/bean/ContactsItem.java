package com.guotion.sicilia.bean;

import java.io.Serializable;

/**
 * @function 联系人 item
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-12 下午3:58:23
 *
 */
public class ContactsItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String imgHead;
	private String accountName;
	private String accountNote;
	
	public ContactsItem() {}

	public ContactsItem(String accountName) {
		this.accountName = accountName;
	}

	public ContactsItem(String imgHead, String accountName) {
		this.imgHead = imgHead;
		this.accountName = accountName;
	}

	public ContactsItem(String imgHead, String accountName, String accountNote) {
		this.imgHead = imgHead;
		this.accountName = accountName;
		this.accountNote = accountNote;
	}

	public String getImgHead() {
		return imgHead;
	}
	
	public void setImgHead(String imgHead) {
		this.imgHead = imgHead;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getAccountNote() {
		return accountNote;
	}
	
	public void setAccountNote(String accountNote) {
		this.accountNote = accountNote;
	}
	
}
