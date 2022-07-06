package com.studio4droid.utils;

import android.content.Context;
import android.content.ClipboardManager;
import android.webkit.JavascriptInterface;

public class ClipBoardUtils {
    
	private static ClipboardManager clipboard;
	
    public static void init(Context context){
		ClipBoardUtils.clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
	}
	
	@JavascriptInterface
	public static void setText(String text){
		clipboard.setText(text);
	}
	
	@JavascriptInterface
	public static String getText(){
		return clipboard.getText().toString();
	}
}
