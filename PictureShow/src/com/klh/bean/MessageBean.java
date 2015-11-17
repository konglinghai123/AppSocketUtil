package com.klh.bean;

import android.net.Uri;

public class MessageBean {
	private String picurl;
	private String message;
	private Boolean IsMe;
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "MessageBean [picurl=" + picurl + ", message=" + message + "]";
	}
	public MessageBean() {
		
	}
	
	public Boolean getIsMe() {
		return IsMe;
	}
	public void setIsMe(Boolean isMe) {
		IsMe = isMe;
	}
	public MessageBean(String picurl, String message, Boolean isMe) {
		super();
		this.picurl = picurl;
		this.message = message;
		IsMe = isMe;
	}
}
