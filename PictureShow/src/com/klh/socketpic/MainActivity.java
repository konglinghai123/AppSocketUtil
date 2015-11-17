package com.klh.socketpic;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pictureshow.R;
import com.klh.adapter.ListViewAdapter;
import com.klh.bean.MessageBean;
import com.klh.socketUtil.SocketBean;
import com.klh.socketUtil.SocketClientService;
import com.klh.socketUtil.SocketDevice;
import com.klh.socketUtil.SocketTools;
import com.klh.util.AppUtils;
import com.klh.util.SdCardUtil;
import com.klh.view.TitleBar;

public class MainActivity extends Activity
{
	

	private int selectindex=0;
	private SocketDevice device=new SocketDevice();
	private Boolean screen=true;
	private Boolean successflag=false;
	private Context context;
	private TitleBar titleBar;
	private	ArrayList<String> userinfo;
	private ArrayList<String> path=new ArrayList<String>();//图片路径数组
	private ArrayList<MessageBean> Mb=new  ArrayList<MessageBean>();//列表数据
	private ImageView view;
	/** Notification的ID */
	int notifyId = 100;
	public NotificationManager mNotificationManager;
	/** Notification构造器 */
	NotificationCompat.Builder mBuilder;
	private ListView list;
	private ListViewAdapter adapter;
	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(final Context context, Intent intent) {
			String action = intent.getAction();
			
			
			if (SocketTools.ACTION_CONNECT_SUCCESS.equals(action)) {
				//连接成功
				Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
				Log.e("TAG",device.getDomainName());
				titleBar.SetCenterText("连接成功");
				successflag=true;
				
				
			} else if (SocketTools.ACTION_DATA_TO_GAME.equals(action)) {
				//接收数据
				Object data = intent.getExtras().getSerializable(SocketTools.DATA);
				String msg = data.toString().trim();
				//if(screen){
				Log.e("msg", msg);
				view.setImageResource(R.drawable.ic_launcher);
				MessageBean b=new MessageBean();
				b.setPicurl("");
					try{
					for(String s:path){
						Log.e("this", s.substring(s.lastIndexOf("/"),s.lastIndexOf(".")));
						Log.e("NOW", msg.substring(msg.indexOf("+"),msg.lastIndexOf("-")));
					if(s.substring(s.lastIndexOf("/")+1,s.lastIndexOf(".")).equals(msg.substring(msg.indexOf("+")+1,msg.lastIndexOf("-"))))
					{	Log.e("success", msg);
						b.setPicurl(s);	
						view.setVisibility(view.VISIBLE);
						ShowImage(s,view);
					}
					}
					if(b.getPicurl().length()!=0&&b.getPicurl()!=""){
						b.setMessage(msg.replace("+", "编号[").replace("-", "]"));
					}else{
						b.setMessage("找不到图片"+"\n"+msg);
					}
					b.setIsMe(false);
					titleBar.SetCenterText(b.getMessage());
					}catch(Exception e){
						titleBar.SetCenterText(msg);
					}
					
			
				
				//Mb.add(b);
				//adapter.notifyDataSetChanged();
				if(!screen){
					showIntentActivityNotify(msg);
				}
				
				
			
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
				
			
			}else if(SocketTools.ACTION_CLEAR_DATA.equals(action)){
				
				
				
			}else if(SocketTools.ACTION_CHANGE_NEWWORK.equals(action)){
				//Toast.makeText(MainActivity.this,"以切换", Toast.LENGTH_SHORT).show();
			}
		}

	
	};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_2);
		// 初始化SlideMenu
		initview();
		initNotify();
		
	}
	
	
	
	private void initview() {
		context=this;
	
		Intent intent=getIntent();
		userinfo = intent.getStringArrayListExtra("user");
		device.setDomainName(userinfo.get(0));
		device.setIp(userinfo.get(0));
		device.setPort(Integer.parseInt(userinfo.get(1)));
		
		if(SdCardUtil.checkSdCard()){
			SdCardUtil.createFileDir(SdCardUtil.dir+ "/"+SdCardUtil.doc);
			Log.e("creat", SdCardUtil.getSdPath()+SdCardUtil.dir+SdCardUtil.doc);
		}else{
			Toast.makeText(MainActivity.this, "SD不可用", Toast.LENGTH_SHORT).show();	
		}
		File f=new File( SdCardUtil.getSdPath()+"/"+SdCardUtil.dir+"/"+SdCardUtil.doc);
		File[] files = f.listFiles();// 列出所有文件  
		for(File file:files){
			path.add(file.getAbsolutePath());
			
		}
		for(String s:path){
			Log.e("file",s);
		}
		titleBar=(TitleBar)findViewById(R.id.TitleBar2);
		
		 titleBar.setBackgroundColor(getResources().getColor(R.color.blue));
		
		 titleBar.showLeft("连接中", getResources().getDrawable(R.drawable.app_back), new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		 view=(ImageView) findViewById(R.id.View);
		// list=(ListView) findViewById(R.id.message);
		 //adapter=new ListViewAdapter(context,Mb);
		 //list.setAdapter(adapter);
		 //list.setOnItemClickListener(new OnItemClickListener(){

			//@Override
			//public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				//	long arg3) {
				//Intent i=new Intent();
				//i.putExtra("path", Mb.get(arg2).getPicurl());
				//i.setClass(context, PictureActivity.class);
				//startActivity(i);
				
			//}
			 
		// });
		 
	
	}

	public void showDialog4(){  
	    final Context context = this;  
	    //定义单选框选项  
	    final String[] singleChoiceItems = {"+AT+SHENG","+AT+SHOW","+AT+JIENG","+ZHTim?","+JIANCETC35","+24XOUT","+AT+QXZDSS","+AT+HFZDSS","+APM"};  
	    int defaultSelectedIndex = 0;//单选框默认值：从0开始  
	   
	    //创建对话框  
	    new AlertDialog.Builder(context)  
	    .setTitle("命令")//设置对话框标题  
	    .setIcon(android.R.drawable.ic_dialog_info)//设置对话框图标  
	    .setSingleChoiceItems(singleChoiceItems, defaultSelectedIndex, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				 selectindex=which;
			}
	    	
	    })
	    
	    .setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(singleChoiceItems[selectindex]=="+APM"){
				
				}else{
				
				}
			}
	    	
	    })//设置对话框[肯定]按钮  
	    .setNegativeButton("取消", null)//设置对话框[否定]按钮  
	    
	    .show();  
	}  
	@SuppressLint("SimpleDateFormat")
	public String TimeStamp2Date(String timestampString){  
		  Long timestamp = Long.parseLong(timestampString);  
		  String date = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(timestamp));  
		  return date;  
		}
	


	@Override
	protected void onDestroy() {
		successflag=false;
		destoryservice();
	
		super.onDestroy();
	}



	@Override
	protected void onStart() {
		
		if(!successflag){
			startservice();
		}
	
		super.onStart();
	}
	@Override
	protected void onStop() {
		screen=false;
		super.onStop();
	}
	@Override
	protected void onResume() {
		
		super.onResume();
	}
	@Override
	protected void onRestart() {
		screen=true;
		super.onRestart();
	}
	/** 显示通知栏点击跳转到指定Activity */
	public void showIntentActivityNotify(String content){
		// Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
		//notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
		mBuilder.setAutoCancel(true)//点击后让通知将消失  
				.setContentTitle("我的监控")
				.setContentText(content)
				.setTicker("通知");
		//点击的意图ACTION是跳转到Intent
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pendingIntent);
		
		mNotificationManager.notify(notifyId, mBuilder.build());
	}
	/** 初始化通知栏 */
	private void initNotify(){
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("测试标题")
				.setContentText("测试内容")
				.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
				.setTicker("测试通知来啦")//通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消  
				.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_SOUND)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
				.setSmallIcon(R.drawable.ic_launcher);
	}
	public PendingIntent getDefalutIntent(int flags){
		PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
		return pendingIntent;
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
	private void getconnect(){
		Intent selectDeviceIntent = new Intent(SocketTools.ACTION_SELECTED_DEVICE);
		selectDeviceIntent.putExtra(SocketTools.DEVICE, device);
		sendBroadcast(selectDeviceIntent);
	
	}
	private void destoryservice(){
		Intent stopService = new Intent(SocketTools.ACTION_STOP_SERVICE);
		sendBroadcast(stopService);
		unregisterReceiver(broadcastReceiver);
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
	private void ShowImage(String path,ImageView myView){
		  if(path.length()!=0&&path!=""&&path!=null){
		        BitmapFactory.Options opts = new Options();
		        // 不读取像素数组到内存中，仅读取图片的信息
		        opts.inJustDecodeBounds = true;
		        BitmapFactory.decodeFile(path, opts);
		        // 从Options中获取图片的分辨率
		        int imageHeight = opts.outHeight;
		        int imageWidth = opts.outWidth;

		        // 获取Android屏幕的服务
		        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		        // 获取屏幕的分辨率，getHeight()、getWidth已经被废弃掉了
		        // 应该使用getSize()，但是这里为了向下兼容所以依然使用它们
		        int windowHeight = wm.getDefaultDisplay().getHeight();
		        int windowWidth = wm.getDefaultDisplay().getWidth();

		        // 计算采样率
		        int scaleX = imageWidth / windowWidth;
		        int scaleY = imageHeight / windowHeight;
		        int scale = 1;
		        // 采样率依照最大的方向为准
		        if (scaleX > scaleY && scaleY >= 1) {
		            scale = scaleX;
		        }
		        if (scaleX < scaleY && scaleX >= 1) {
		            scale = scaleY;
		        }

		        // false表示读取图片像素数组到内存中，依照设定的采样率
		        opts.inJustDecodeBounds = false;
		        // 采样率
		        opts.inSampleSize = scale;
		        Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
		        myView .setImageBitmap(bitmap);
		        }
	}
}
