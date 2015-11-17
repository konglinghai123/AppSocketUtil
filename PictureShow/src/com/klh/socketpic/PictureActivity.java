package com.klh.socketpic;

import com.example.pictureshow.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PictureActivity extends Activity{
	private ImageView myView = null;
	private String path="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	  requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);
      
        myView = (ImageView)findViewById(R.id.myView);
        Intent i=getIntent();
        path=i.getStringExtra("path");
        Log.e("Path", path);
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
