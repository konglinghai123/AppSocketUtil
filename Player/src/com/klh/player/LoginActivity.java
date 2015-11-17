package com.klh.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.klh.util.AppUtils;
import com.klh.util.SharedPreferenceDb;
import com.klh.view.EditTextWithDel;
import com.klh.view.TitleBar;




import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;



@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class LoginActivity extends Activity implements OnClickListener {


	
	private Button btnLogin;
	private CheckBox ck;
	private EditTextWithDel etPhone;
	private  Button btnconnect;
	private EditTextWithDel etPassWord;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
	
		
		setContentView(R.layout.userlogin);
		
		initViews();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		TitleBar titleBar=(TitleBar)findViewById(R.id.TitleBar);
		
		 titleBar.setBackgroundColor(getResources().getColor(R.color.blue));
		 
		 titleBar.showLeft("登陆", getResources().getDrawable(R.drawable.app_back), new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		 
	}
	
	public void initViews() {
	
		
		
		// tvRegister=(RelativeLayout)findViewById(R.id.newUserRegister);
		 //tvRegister.setOnClickListener(this);
		 btnconnect=(Button) findViewById(R.id.connect);
		 btnconnect.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
			}
			 
		 });
		 etPhone=(EditTextWithDel)findViewById(R.id.accounts);
		 etPassWord=(EditTextWithDel)findViewById(R.id.password);
		 ck=(CheckBox) findViewById(R.id.ck);
		 ck.setChecked(true);
		 etPhone.setText(new SharedPreferenceDb(LoginActivity.this).getUserId());
		 etPassWord.setText(new SharedPreferenceDb(LoginActivity.this).getName());
		 btnLogin=(Button)findViewById(R.id.login);
		 btnLogin.setOnClickListener(this);
		 
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
		 if(view==btnLogin){
			
			if(!etPhone.getText().toString().isEmpty()){
				
				if(!etPassWord.getText().toString().isEmpty()){
					
					
					if(AppUtils.checkNetwork(LoginActivity.this)==true){
					
						
	
						

									ArrayList<String> userinfo=new ArrayList<String>();
									userinfo.add(etPhone.getText().toString().trim());
									userinfo.add(etPassWord.getText().toString().trim());
									if(ck.isChecked()){
									new SharedPreferenceDb(LoginActivity.this).setUserId(etPhone.getText().toString().trim());
									new SharedPreferenceDb(LoginActivity.this).setName(etPassWord.getText().toString().trim());
									}else{
										new SharedPreferenceDb(LoginActivity.this).setUserId("");
										new SharedPreferenceDb(LoginActivity.this).setName("");
									}
									Intent intent=new Intent();
									intent.putStringArrayListExtra("user",userinfo);
									
									intent.setClass(LoginActivity.this, MainActivity.class);
									startActivity(intent);
									finish();
										
									
							
					
						
						
					}else{
						showToast("亲，您还没有联网了!");
					}
					
				}else{
					showToast("端口不能为空");
				}
			}else{
				showToast("IP地址不能为空");
			}
			

			
			
		}
			
		}
	
	
	public void showToast(String str){
		Toast.makeText(LoginActivity.this, str, Toast.LENGTH_LONG).show();
	}
	
	
}
