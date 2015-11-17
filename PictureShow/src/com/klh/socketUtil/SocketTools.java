package com.klh.socketUtil;

public class SocketTools {

	
	//设备
	public static final String DEVICE = "device";
	

	
	/**
	 * 字符串常量，Intent中的数据
	 */
	public static final String DATA = "data";
	
	/**
	 * Action类型标识符，Action类型 为读到数据
	 */
	public static final String ACTION_READ_DATA = "read";
	
	/**
	 * Action：选择的用于连接的设备
	 */
	public static final String ACTION_SELECTED_DEVICE = "select";
	
	/**
	 * Action：开启服务器
	 */
	public static final String ACTION_START_SERVER = "start";
	
	/**
	 * Action：关闭后台Service
	 */
	public static final String ACTION_STOP_SERVICE = "stop";
	
	/**
	 * Action：到Service的数据
	 */
	public static final String ACTION_DATA_TO_SERVICE = "send";
	
	/**
	 * Action：到游戏业务中的数据
	 */
	public static final String ACTION_DATA_TO_GAME = "recevice";
	
	/**
	 * Action：连接成功
	 */
	public static final String ACTION_CONNECT_SUCCESS = "success";
	
	/**
	 * Action：连接错误
	 */
	public static final String ACTION_CONNECT_ERROR = "error";
	/**
	 * Action：准备连接
	 */
	public static final String ACTION_PREPER_CONNECT = "connect";
	/**
	 * Action：清空数据
	 */
	public static final String ACTION_CLEAR_DATA = "delect";
	/**
	 * Action：切换网络
	 */
	public static final String ACTION_CHANGE_NEWWORK = "change";
	/**
	 * Message类型标识符，连接成功
	 */
	public static final int MESSAGE_CONNECT_SUCCESS = 0x00000010;
	
	/**
	 * Message：连接失败
	 */
	public static final int MESSAGE_CONNECT_ERROR = 0x00000013;
	
	/**
	 * Message：读取到一个对象
	 */
	public static final int MESSAGE_READ_OBJECT = 0x00000014;
	/**
	 * Message:后台创建成功
	 */
	public static final int MESSAGE_CREAT_SUCCESS = 0x00000015;
	/**
	 * Message类型标识符，连接成功
	 */
	public static final int MESSAGE_NETWORK_CHANGE = 0x00000012;
}
