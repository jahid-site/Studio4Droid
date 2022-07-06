package com.studio4droid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;

public class DBUtils {
	
	private static SharedPreferences db;
	
    public static void init(Context context){
		DBUtils.db = context.getSharedPreferences("database",Context.MODE_PRIVATE);
	}
    
	@JavascriptInterface
	public static void setData(String key,String value){
		db.edit().putString(key,value).apply();
	}
	
	@JavascriptInterface
	public static String getData(String key, String defaultValue){
		return db.getString(key,defaultValue);
	}
	
	@JavascriptInterface
	public static void remove(String key){
		db.edit().remove(key).apply();
	}
	
	@JavascriptInterface
	public static void clear(){
		db.edit().clear().apply();
	}
}
