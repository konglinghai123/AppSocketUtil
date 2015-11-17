package com.klh.util;

import java.io.File;

import android.os.Environment;

public class SdCardUtil {

	
	//项目文件根目录
	public static final String dir="/我的监控";
	
	//照相机照片目录
	public static final String doc="/images";
	

	
	/**
	 * 检测sd卡是否可以用
	 * 
	 * @return
	 */
	public static boolean checkSdCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd card 可用
			return true;
		} else {
			// 当前不可用
			return false;
		}
	}

	/**
	 * 获取sd卡文件路径
	 * 
	 * @return
	 */
	public static String getSdPath() {
		return Environment.getExternalStorageDirectory()+"/";
	}

	/**
	 * 创建一个项目文件夹
	 * 
	 * @param fileDir
	 *            文件目录名
	 */
	public static void createFileDir(String fileDir) {
		String path = getSdPath() + fileDir;
		File path1 = new File(path);
		if (!path1.exists()) {
			path1.mkdirs();
			
		}
	}
	
	
	
}
