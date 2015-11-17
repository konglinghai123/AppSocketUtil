package com.klh.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketHeartTest extends Thread{
	private Handler serviceHandler;	
	 private long sendTime = 0L;
	private static final long HEART_BEAT_RATE = 60 * 1000;
	private Socket socket;	
 
	public SocketHeartTest(Handler serviceHandler, Socket socket){
		this.serviceHandler=serviceHandler;
		this.socket=socket;
	}
	@Override
	public void run() {
		if(socket!=null){
		
		while(true){
		 if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
			 write();
			 sendTime=System.currentTimeMillis();    
               
         }
		}
		}else{
			 Log.e("Tag", "连接断开");
			 serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
		}
	}
	public void write() {
		 try {
			 if(socket.isConnected()==false){
       		 Log.e("Tag", "连接断开");
       		 serviceHandler.obtainMessage(SocketTools.MESSAGE_CONNECT_ERROR).sendToTarget();
       		
       	}else{
			  byte [] sb=new byte[512];
			SocketBean bt=new SocketBean();
			bt.setMsg("+AT+1");
            OutputStream outStream = socket.getOutputStream();
            outStream.flush();
             sb=bt.getMsg().getBytes("GBK");
            outStream.write(sb);
            outStream.flush();
       	}
            
           
        } catch (IOException e) {
           
            Log.e("TAG", e.toString());
            e.printStackTrace();
        }
	}
	public void revice(){
		
	       
      
	}
}
