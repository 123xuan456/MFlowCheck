package com.mtm.flowcheck.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 自定义的Sharedperference工具类
 */
public class ShareUtil {
	public static String CONFIG =  "config";
	private static SharedPreferences sharedPreferences;
	
	public static void saveBooleanData(Context context, String key, boolean value){
		if(sharedPreferences==null){
			sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putBoolean(key, value).commit();
	}
	
	public static boolean getBooleanData(Context context, String key, boolean defValue){
		if(sharedPreferences==null){
			sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getBoolean(key, defValue);
	}

	public static void saveStringData(Context context, String key, String value){
		if(sharedPreferences==null){
			sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putString(key, value).commit();
	}

	public static String getStringData(Context context, String key, String defValue){
		if(sharedPreferences==null){
			sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getString(key, defValue);
	}
	public static void saveIntData(Context context, String key, int value){
		if(sharedPreferences==null){
			sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putInt(key, value).commit();
	}

	public static int getIntData(Context context, String key, int defValue){
		if(sharedPreferences==null){
			sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getInt(key, defValue);
	}

	public static void saveFileInfo(Context context, String key, int value){
		SharedPreferences sPreferences=context.getSharedPreferences("config",  Context.MODE_MULTI_PROCESS | Activity.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor=sPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getFileInfo(Context context, String key){
		SharedPreferences sPreferences=context.getSharedPreferences("config", Context.MODE_MULTI_PROCESS | Activity.MODE_WORLD_READABLE);
		int value = sPreferences.getInt(key,12);
		return value;
	}
}
