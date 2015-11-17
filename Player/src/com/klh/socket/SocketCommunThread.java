package com.klh.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.klh.util.AppUtils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketCommunThread extends Thread {
	private Handler serviceHandler;
	private Socket socket;
	public volatile boolean isRun = true;//运行标志位
	private String CurrentNetwork;
	private Context context;
	private SocketDevice device=new SocketDevice();
	public SocketCommunThread(Handler handler,Socket socket,Context context) {
		this.serviceHandler = handler;
		this.socket = socket;
		this.context=context;
	}
	@Override
	public void run() {
		if(socket!=null){
			device.setPort(socket.getLocalPort());
			device.setIp(socket.getInetAddress().toString());
			
		 while (true) {
			 if(AppUtils.checkNetwork(context)){
				
		     try {
		    	InputStream inputStream = socket.getInputStream();
		    	
		    	
		    	byte[] b= new byte[512];
		    	
		    	if(inputStream.available()>0 == false)
		    	{	
		    		
		    	  continue;
		    	}
		    	else{
		      		Thread.sleep(200);
		      		
		    	}
		    	/*int code=inputStream.available();
		    	if(code<0){
		    		 serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
		    		 
		    		 Log.e("error","服务端断线");
		    		 return;
		    	}else if(code>0 == false){
		    		 continue;
		    	}else{
		    		Thread.sleep(200);
		    	}*/
		        inputStream.read(b);
		     
		        String data=new String(b,"GBK");
		        Log.e("Tab",data);
		        b=null;
		       
		            Message msg = serviceHandler.obtainMessage();
		             msg.what = SocketTools.MESSAGE_READ_OBJECT;
		             msg.obj = data;
		             msg.sendToTarget();
		          
		    	
		         
		    
		     } catch (IOException e) {
		    	 serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
		         Log.e("TAG", e.toString());
		        return;
		     } catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}catch(Exception e){
				 serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
				 return;
			}
			 }else{
				 serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
				 return;
			 }
		 }
	}else{
		//发送连接失败消息
		serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
	}
		if (socket != null) {
			try {
				socket.close();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//判断手机网络是否可用
		public boolean isNetAvailable(Context context) {  
	        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
	        NetworkInfo info = manager.getActiveNetworkInfo(); 
	        if(info!=null){
	        CurrentNetwork=info.getTypeName();
	        }
	        return (info != null && info.isAvailable());  
	    }
	public void writeObject(Object obj) {
		 try {
			 if(socket.isConnected()==false){
        		 Log.e("Tag", "连接断开");
        		 serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
        		
        	}else{
        		
			  byte [] sb=new byte[512];
			SocketBean bt=(SocketBean)obj;
             OutputStream outStream = socket.getOutputStream();
             outStream.flush();
              sb=bt.getMsg().getBytes("GBK");
             outStream.write(sb);
             outStream.flush();
        	}
             
            
         } catch (IOException e) {
        	 serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
             Log.e("TAG", e.toString());
             e.printStackTrace();
         }
	
	
}
}
