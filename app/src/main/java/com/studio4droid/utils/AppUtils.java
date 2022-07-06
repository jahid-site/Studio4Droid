package com.studio4droid.utils;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import com.studio4droid.MainActivity;
import com.studio4droid.utils.WebViewUtils;

public class AppUtils {
    
	private static Activity activity;
    
	public static void init(Activity activity){
		AppUtils.activity = activity;
	}
	
	@JavascriptInterface
	public static void finish(){
		activity.finish();
	}
	
	@JavascriptInterface
	public static void changeTheme(String theme){
		DBUtils.setData("theme",theme);
		activity.finish();
		activity.startActivity(new Intent(activity,MainActivity.class));
	}
	
	@JavascriptInterface
	public static void reload(){
		WebViewUtils.getWebview().clearSslPreferences();
		WebViewUtils.getWebview().clearAnimation();
		WebViewUtils.getWebview().clearDisappearingChildren();
		WebViewUtils.getWebview().clearFormData();
		WebViewUtils.getWebview().clearCache(true);
		WebViewUtils.getWebview().clearFocus();
		WebViewUtils.getWebview().clearHistory();
		WebViewUtils.getWebview().clearMatches();
		WebViewUtils.getWebview().clearView();
		WebViewUtils.getWebview().removeAllViews();
		WebViewUtils.getWebview().removeAllViewsInLayout();
		WebViewUtils.getWebview().reload();
	}
	
}
