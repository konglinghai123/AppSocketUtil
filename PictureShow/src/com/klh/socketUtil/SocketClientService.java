package com.klh.socketUtil;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


public class SocketClientService extends Service {
	
	
	

	//蓝牙通讯线程
	private SocketCommunThread communThread;
	private SocketClientConnThread clientconnThread;
	private Context context;
	private Socket socket;
	public SocketDevice device; 
	//控制信息广播的接收器
	private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
		 if (SocketTools.ACTION_SELECTED_DEVICE.equals(action)) {
				//选择了连接的服务器设备
			
				 device =  (SocketDevice) intent.getExtras().get(SocketTools.DEVICE);
			
				 Log.e("TAG","开始连接");
				//开启设备连接线程
				 clientconnThread= new SocketClientConnThread(handler, device);
				 clientconnThread.start();
				
			} else if (SocketTools.ACTION_STOP_SERVICE.equals(action)) {
				//停止后台服务
				if (communThread != null) {
					communThread.isRun = false;
					
				}
				if(clientconnThread!=null){
					clientconnThread.isRun=false;
				}
				Log.e("colse","关闭中");
				stopSelf();
				
			} else if (SocketTools.ACTION_DATA_TO_SERVICE.equals(action)) {
				//获取数据
				Object data = intent.getSerializableExtra(SocketTools.DATA);
			
				if (communThread != null) {
					communThread.writeObject(data);
				}
				
			}
		}
	};
	
	//接收其他线程消息的Handler
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			//处理消息
			switch (msg.what) {
			case SocketTools.MESSAGE_CONNECT_ERROR:
				//连接错误
				//发送连接错误广播
				Log.e("TAG","连接断开");
				//开启设备连接线程
				 clientconnThread= new SocketClientConnThread(handler, device);
				 clientconnThread.start();
				Intent errorIntent = new Intent(SocketTools.ACTION_CONNECT_ERROR);
				sendBroadcast(errorIntent);
				break;
			case SocketTools.MESSAGE_CONNECT_SUCCESS:
				//连接成功
				
				//开启通讯线程
				Log.e("Tag", "开始通讯");
			
				communThread = new SocketCommunThread(handler, (Socket)msg.obj,context);
				communThread.start();
				
				//发送连接成功广播
				Intent succIntent = new Intent(SocketTools.ACTION_CONNECT_SUCCESS);
				sendBroadcast(succIntent);
				break;
			case SocketTools.MESSAGE_READ_OBJECT:
				//读取到对象
				//发送数据广播（包含数据对象）
				Intent dataIntent = new Intent(SocketTools.ACTION_DATA_TO_GAME);
				dataIntent.putExtra(SocketTools.DATA, (Serializable)msg.obj);
				sendBroadcast(dataIntent);
				break;
			case SocketTools.MESSAGE_CREAT_SUCCESS:
				//后台创建成功
				//发送准备连接广播给ACTIVITY
				Intent connectIntent=new Intent(SocketTools.ACTION_PREPER_CONNECT);
				sendBroadcast( connectIntent);
			
				break;
			case SocketTools.MESSAGE_NETWORK_CHANGE:
				//网络发送变化时
				//发送重新连接请求
				Intent changeIntent=new Intent(SocketTools.ACTION_CHANGE_NEWWORK);
				sendBroadcast(changeIntent);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 获取通讯线程
	 * @return
	 */
	public SocketCommunThread getBluetoothCommunThread() {
		return communThread;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		
		super.onStart(intent, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	/**
	 * Service创建时的回调函数
	 */
	@Override
	public void onCreate() {	
		//controlReceiver的IntentFilter
		IntentFilter controlFilter = new IntentFilter();
	
		controlFilter.addAction(SocketTools.ACTION_SELECTED_DEVICE);
		controlFilter.addAction(SocketTools.ACTION_STOP_SERVICE);
		controlFilter.addAction(SocketTools.ACTION_DATA_TO_SERVICE);
		registerReceiver(controlReceiver, controlFilter);
		handler.obtainMessage(SocketTools.MESSAGE_CREAT_SUCCESS).sendToTarget();
		Log.e("TAG","创建成功");
		context=this;
		super.onCreate();
	}
	
	/**
	 * Service销毁时的回调函数
	 */
	@Override
	public void onDestroy() {
		if (communThread != null) {
			communThread.isRun = false;
		}
		//解除绑定
		unregisterReceiver(controlReceiver);
	
		Log.e("Tag","销毁成功");
		System.exit(0);
		super.onDestroy();
	}

}