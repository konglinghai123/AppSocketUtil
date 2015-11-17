package com.klh.socketUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketClientConnThread extends Thread{
	public volatile boolean isRun = true;//���б�־λ
	private Handler serviceHandler;		
	private SocketDevice serverDevice;	
	private Socket socket;		
	public SocketClientConnThread(Handler handler, SocketDevice sd) {
		this.serviceHandler=handler;
		this.serverDevice=sd;
	}

	@Override
	public void run() {
			if(serverDevice.getDomainName()!=null||serverDevice.getDomainName()!=""){
				try 
				{ 
			
				InetAddress address=InetAddress.getByName(serverDevice.getDomainName());
			
				serverDevice.setIp(address.getHostAddress());
				} 
				catch (Exception e) 
				{ 
				Log.e("Tag", "error");
				} 
			}
	
		try {
			 Log.e("TAG","正常");
			 Log.e("TAG","Ip:"+serverDevice.getIp()+"端口:"+String.valueOf(serverDevice.getPort()));
			socket = new Socket(serverDevice.getIp(),serverDevice.getPort());
			 Log.e("TAG","正常");
			Message msg = serviceHandler.obtainMessage();
			msg.what = SocketTools.MESSAGE_CONNECT_SUCCESS;
			msg.obj = socket;
			msg.sendToTarget();
			

			
		} catch (Exception ex) {
			Log.e("TAG", ex.toString());
			//��������ʧ����Ϣ
			serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			
		}
		
		//�������ӳɹ���Ϣ����Ϣ��obj����Ϊ���ӵ�socket
		
	}

}
