package com.klh.socket;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SocketBean	implements Serializable {
private String msg = "";
	
	public SocketBean(String string) {
	// TODO 自动生成的构造函数存根
}

	public SocketBean() {
		// TODO 自动生成的构造函数存根
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
}
