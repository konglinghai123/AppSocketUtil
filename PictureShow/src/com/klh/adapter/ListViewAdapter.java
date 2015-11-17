package com.klh.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


import com.example.pictureshow.R;
import com.klh.bean.MessageBean;
import com.klh.socketpic.PictureActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private List<MessageBean> allData;
	 int[] to={R.id.chatlist_image_me,R.id.chatlist_text_me,R.id.chatlist_image_other,R.id.chatlist_text_other};
	 int[] layout={R.layout.chat_item_me,R.layout.chat_item_other};
	 private Context context;
	public ListViewAdapter(Context context,List<MessageBean> allImages) {
		// TODO Auto-generated constructor stub
		layoutInflater=LayoutInflater.from(context);
		this.allData=allImages;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return allData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return allData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(contentView==null){
			viewHolder=new ViewHolder();
			if(allData.get(arg0).getIsMe()){
				contentView=layoutInflater.inflate(layout[0], null);
				viewHolder.imageView=(ImageView)contentView.findViewById(to[0]);
				viewHolder.text=(TextView)contentView.findViewById(to[1]);
				contentView.setTag(viewHolder);
			}else{
				contentView=layoutInflater.inflate(layout[1], null);
				viewHolder.imageView=(ImageView)contentView.findViewById(to[2]);
				viewHolder.text=(TextView)contentView.findViewById(to[3]);
				contentView.setTag(viewHolder);
			}
			
		}else{
			viewHolder=(ViewHolder)contentView.getTag();
		}
		if(allData.get(arg0).getPicurl().length()!=0){
			Bitmap bitmap = getLoacalBitmap(allData.get(arg0).getPicurl());
			viewHolder.imageView.setImageBitmap(bitmap);
			final String path=allData.get(arg0).getPicurl();
			viewHolder.imageView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
					Intent i=new Intent();
					i.putExtra("path", path);
					i.setClass(context, PictureActivity.class);
					context.startActivity(i);
				}
				
			});
				
		
		}else{
			viewHolder.imageView.setVisibility(View.GONE);
		}
		
		viewHolder.text.setText(allData.get(arg0).getMessage());
		return contentView;
	}
	public static Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	static class ViewHolder{
		
		private ImageView imageView;
		private TextView text;
	}

}
