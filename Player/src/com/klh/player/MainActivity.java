package com.klh.player;

import java.util.ArrayList;
import java.util.HashMap;


import com.klh.socket.SocketBean;
import com.klh.socket.SocketClientService;
import com.klh.socket.SocketDevice;
import com.klh.socket.SocketTools;

import com.klh.util.AppUtils;
import com.klh.view.TitleBar;

import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(final Context context, Intent intent) {
			String action = intent.getAction();
			
			
			if (SocketTools.ACTION_CONNECT_SUCCESS.equals(action)) {
				//连接成功
				successflag=true;
				titleBar.SetCenterText("天籁电子");
				
			} else if (SocketTools.ACTION_DATA_TO_GAME.equals(action)) {
				//接收数据
				Object data = intent.getExtras().getSerializable(SocketTools.DATA);
				String msg = data.toString().trim();
				
			}else if(SocketTools.ACTION_CONNECT_ERROR.equals(action)){
				Toast.makeText(MainActivity.this, "连接断开", Toast.LENGTH_SHORT).show();	
				titleBar.SetCenterText("尝试重连中");
			} else if(SocketTools.ACTION_PREPER_CONNECT.equals(action)){
				Log.e("TAG","准备连接");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				if(AppUtils.checkNetwork(context)){
					Toast.makeText(MainActivity.this, "网路可用", Toast.LENGTH_SHORT).show();
					getconnect();
				}else{
					Toast.makeText(MainActivity.this, "网路不可用", Toast.LENGTH_SHORT).show();
					new Thread(){

		                  public void run(){
		                		while(true){
		        					if(AppUtils.checkNetwork(context)){
		        						
		        						getconnect();
		        		
		        						break;
		        					}
		        				}

		                    };

		          }.start();

				}
			}
		}

		

	
	};
	private Boolean successflag=false;
	private	ArrayList<String> userinfo;
	private SocketDevice device=new SocketDevice();
	private TitleBar titleBar;
	private HashMap<String,Button> map=new HashMap<String,Button>();//所有按钮的集合
	private HashMap<String,CheckBox> chmap=new HashMap<String,CheckBox>();//所有checkbox的集合
	String [] stauts=new String [8];
	private EditText result;
	private String songid="";
	private LinearLayout controlpage;
	private boolean IscontrolpageShow=false;
	private boolean IsInputAllow=false;
	private LinearLayout.LayoutParams  linearParams;
	private int edHeight=0;
	private String command="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initview();
		initevent();
		initdate();
	}
	private void getconnect(){
		Intent selectDeviceIntent = new Intent(SocketTools.ACTION_SELECTED_DEVICE);
		selectDeviceIntent.putExtra(SocketTools.DEVICE, device);
		sendBroadcast(selectDeviceIntent);
	
	}
	private void initview(){
		 titleBar=(TitleBar)findViewById(R.id.TitleBar1);
		 Button bt1=(Button) findViewById(R.id.btn_1);
		 Button bt2=(Button) findViewById(R.id.btn_2);
		 Button bt3=(Button) findViewById(R.id.btn_3);
		 Button bt4=(Button) findViewById(R.id.btn_4);
		 Button bt5=(Button) findViewById(R.id.btn_5);
		 Button bt6=(Button) findViewById(R.id.btn_6);
		 Button bt7=(Button) findViewById(R.id.btn_7);
		 Button bt8=(Button) findViewById(R.id.btn_8);
		 Button bt9=(Button) findViewById(R.id.btn_9);
		 Button bt0=(Button) findViewById(R.id.btn_0);
		 Button bt_paush=(Button) findViewById(R.id.btn_pause);
		 Button bt_del=(Button) findViewById(R.id.btn_clear);
		 Button bt_stop=(Button) findViewById(R.id.btn_stop);
		 Button bt_play=(Button) findViewById(R.id.btn_play);
		 Button bt_controlpage=(Button) findViewById(R.id.btn_contorl);
		 Button bt_all=(Button) findViewById(R.id.all);
		 Button bt_not=(Button) findViewById(R.id.not);
		 Button bt_ok=(Button) findViewById(R.id.ok);
		 Button bt_send=(Button) findViewById(R.id.send);
		 CheckBox ch1=(CheckBox) findViewById(R.id.ch_1);
		 CheckBox ch2=(CheckBox) findViewById(R.id.ch_2);
		 CheckBox ch3=(CheckBox) findViewById(R.id.ch_3);
		 CheckBox ch4=(CheckBox) findViewById(R.id.ch_4);
		 CheckBox ch5=(CheckBox) findViewById(R.id.ch_5);
		 CheckBox ch6=(CheckBox) findViewById(R.id.ch_6);
		 CheckBox ch7=(CheckBox) findViewById(R.id.ch_7);
		 CheckBox ch8=(CheckBox) findViewById(R.id.ch_8);
		 map.put("0", bt0);
		 map.put("1", bt1);
		 map.put("2", bt2);
		 map.put("3", bt3);
		 map.put("4", bt4);
		 map.put("5", bt5);
		 map.put("6", bt6);
		 map.put("7", bt7);
		 map.put("8", bt8);
		 map.put("9", bt9);
		 map.put("pause", bt_paush);
		 map.put("del",bt_del );
		 map.put("stop", bt_stop);
		 map.put("play", bt_play);
		 map.put("contorl", bt_controlpage);
		 map.put("all",  bt_all);
		 map.put("not", bt_not);
		 map.put("ok",  bt_ok);
		 map.put("send", bt_send);
		 chmap.put("1", ch1);
		 chmap.put("2", ch2);
		 chmap.put("3", ch3);
		 chmap.put("4", ch4);
		 chmap.put("5", ch5);
		 chmap.put("6", ch6);
		 chmap.put("7", ch7);
		 chmap.put("8", ch8);
		 result=(EditText) findViewById(R.id.result);
		 linearParams =(LinearLayout.LayoutParams) result.getLayoutParams(); //取控件当前的布局参数
		 edHeight=linearParams.height;
		 linearParams.height = 370;// 控件的高强制设成20  
		 result.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre> 
		
		 controlpage = (LinearLayout) findViewById(R.id.controlpage);
	}
	private void initevent(){
		for(String id:map.keySet()){
			map.get(id).setOnClickListener(this);
		}
		 titleBar=(TitleBar)findViewById(R.id.TitleBar1);
			
		 titleBar.setBackgroundColor(getResources().getColor(R.color.blue));
		 
		 titleBar.showLeft("连接中", getResources().getDrawable(R.drawable.app_back), new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	}
	private void initdate(){
		Intent intent=getIntent();
		userinfo = intent.getStringArrayListExtra("user");
		device.setDomainName(userinfo.get(0));
		device.setIp(userinfo.get(0));
		device.setPort(Integer.parseInt(userinfo.get(1)));
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.btn_1:
		
			result.setText(append("1"));
			
			break;
		case R.id.btn_2:
			result.setText(append("2"));
			break;
		case R.id.btn_3:
			result.setText(append("3"));
			break;
		case R.id.btn_4:
			result.setText(append("4"));
			break;
		case R.id.btn_5:
			result.setText(append("5"));
			break;
		case R.id.btn_6:
			result.setText(append("6"));
			break;
		case R.id.btn_7:
			result.setText(append("7"));
			break;
		case R.id.btn_8:
			result.setText(append("8"));
			break;
		case R.id.btn_9:
			result.setText(append("9"));
			break;
		case R.id.btn_0:
			result.setText(append("0"));
			break;
		case R.id.btn_pause:
			String command_pause="PAUSE\r\n";
			senddata( command_pause);
			result.setText("暂停["+songid+"]");
			break;
		case R.id.btn_stop:
			String command_stop="STOP\r\n";
			senddata(command_stop);
			result.setText("停止["+"--"+"]");
			break;
		case R.id.btn_clear:
			if(result.getText().length()>0){
				result.setText(result.getText().toString().substring(0,result.getText().toString().length()-1 ));
				if(songid.length()>0){
				songid=songid.substring(0,songid.length()-1 );
				}
			}else{
			result.setText("");
			songid="";
			}
			break;
		case R.id.btn_play:
			
			result.setText("播放["+songid+"]");
			if(songid.length()==1||songid.length()==2){
				String command_play="PLAY+"+songid+"\r\n";
				senddata(command_play);
			
			}else{
				Toast.makeText(MainActivity.this, "歌曲超出范围", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_contorl:
			if(!IscontrolpageShow){
			linearParams.height =edHeight;
			result.setLayoutParams(linearParams); 
			controlpage.setVisibility(View.VISIBLE);
			IscontrolpageShow=true;
			map.get("contorl").setText("隐藏");
			}else{
				linearParams.height =370;
				result.setLayoutParams(linearParams); 
				controlpage.setVisibility(View.GONE);
				IscontrolpageShow=false;
				map.get("contorl").setText("分区控制");
			}
			break;
		case R.id.all:
			for(String id:chmap.keySet()){
				chmap.get(id).setChecked(true);
				
			}
		String command_all="CONTROL+ON\r\n";
		senddata(command_all);
			break;
		case R.id.not:
			for(String id:chmap.keySet()){
				chmap.get(id).setChecked(false);
				
			}
			String command_off="CONTROL+OFF\r\n";
			senddata(command_off);
			break;
		case R.id.ok:
			for(int i=0;i<8;i++){
				
				if(chmap.get(String.valueOf(i+1)).isChecked()){
					stauts[i]="1";
				}else{
					stauts[i]="0";
				}
			}
			String contorl="";
			for(String s:stauts){
				contorl=contorl+","+s;
			}
			contorl="CONTROL+"+contorl.substring(1)+"\r\n";
			senddata(contorl);
			break;
		case R.id.send:
			if(!IsInputAllow){
				
				result.setFocusable(true);
				
				result.setFocusableInTouchMode(true);
				result.requestFocus();
				result.requestFocusFromTouch();
				result.setCursorVisible(true);
				IsInputAllow=true;
				map.get("send").setText("发送");
				}else{
				
					result.setFocusable(false);
					result.setFocusableInTouchMode(false);
					result.setCursorVisible(false);
					IsInputAllow=false;
					map.get("send").setText("输入");
					senddata("<G>"+result.getText().toString()+"-OK");
					result.setText("");
				}
			break;
		}
		
	}
	private String append(String text){
		
		if(songid.length()>=2){
			songid=songid.substring(1, 2)+text;
			return songid;
		}else{
			songid=songid+text;
			return songid;
		}
		
	}
	private void startservice(){
		//注册BoradcasrReceiver
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SocketTools.ACTION_DATA_TO_GAME);
		intentFilter.addAction(SocketTools.ACTION_SELECTED_DEVICE);
		intentFilter.addAction(SocketTools.ACTION_CONNECT_SUCCESS);
		intentFilter.addAction(SocketTools.ACTION_CONNECT_ERROR);
		intentFilter.addAction(SocketTools.ACTION_PREPER_CONNECT);
		intentFilter.addAction(SocketTools.ACTION_CLEAR_DATA);
		intentFilter.addAction(SocketTools.ACTION_CHANGE_NEWWORK);
		registerReceiver(broadcastReceiver, intentFilter);
		Intent startService = new Intent(MainActivity.this, SocketClientService.class);
		startService(startService);
		
	
	}
	private void destoryservice(){
		Intent stopService = new Intent(SocketTools.ACTION_STOP_SERVICE);
		sendBroadcast(stopService);
		unregisterReceiver(broadcastReceiver);
	}
	private void senddata(String command){
		SocketBean data = new SocketBean();
		data.setMsg(command);
		Intent sendDataIntent = new Intent(SocketTools.ACTION_DATA_TO_SERVICE);
		sendDataIntent.putExtra(SocketTools.DATA, data);
		sendBroadcast(sendDataIntent);
	}
	@Override
	public void onBackPressed() {
		Builder dialog = new AlertDialog.Builder(MainActivity.this)
		.setMessage("是否确定退出应用")
		.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				finish();
			}
		})
		.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
			
	}
	@Override
	protected void onDestroy() {
		destoryservice();
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
	}
	@Override
	protected void onStart() {
		if(!successflag){
		startservice();
		}
		super.onStart();
	}

	
}
