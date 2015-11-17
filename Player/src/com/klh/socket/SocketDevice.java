package com.klh.socket;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SocketDevice implements Serializable{
	private String ip;
	private int port;
	private String DomainName;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDomainName() {
		return DomainName;
	}
	public void setDomainName(String domainName) {
		DomainName = domainName;
	}
	public SocketDevice(String ip, int port, String domainName) {
		super();
		this.ip = ip;
		this.port = port;
		DomainName = domainName;
	}
	public SocketDevice() {
		// TODO 自动生成的构造函数存根
	}
	@Override
	public String toString() {
		return "SocketDevice [ip=" + ip + ", port=" + port + ", DomainName="
				+ DomainName + "]";
	}
}
