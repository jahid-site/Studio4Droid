package com.studio4droid.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.studio4droid.R;
import com.studio4droid.utils.AppUtils;
import com.studio4droid.utils.ClipBoardUtils;
import com.studio4droid.utils.DBUtils;
import com.studio4droid.utils.FileUtils;
import com.studio4droid.utils.IntentUitls;
import com.studio4droid.utils.PermissionUtils;
import com.studio4droid.utils.ToastUtils;
import com.studio4droid.utils.VibratorUtils;
import com.studio4droid.utils.clients.ControllWebChromeClient;
import com.studio4droid.utils.clients.ControllWebViewClient;

public class WebViewUtils {
	private static Activity activity;
	private static WebView webview;
    
	public static void inti(Activity activity){
		WebViewUtils.activity = activity;
		CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.setAcceptFileSchemeCookies(true);
		PermissionUtils.inti(activity);
		AppUtils.init(activity);
		FileUtils.init(activity);
		ToastUtils.init(activity);
		ClipBoardUtils.init(activity);
		VibratorUtils.init(activity);
		IntentUitls.init(activity);
		initWebView();
	}
	
	private static void initWebView(){
		webview = (WebView) activity.findViewById(R.id.webview);
		webview.setWebViewClient(new ControllWebViewClient(activity));
		webview.setWebChromeClient(new ControllWebChromeClient(activity));
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setAllowFileAccess(true);
		webview.getSettings().setAllowFileAccessFromFileURLs(true);
		webview.getSettings().setAllowContentAccess(true);
		webview.getSettings().setDomStorageEnabled(true);
		webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
		webview.addJavascriptInterface(new AppUtils(),"AppUtils");
		webview.addJavascriptInterface(new ToastUtils(),"ToastUtils");
		webview.addJavascriptInterface(new FileUtils(),"FileUtils");
		webview.addJavascriptInterface(new DBUtils(),"DBUtils");
		webview.addJavascriptInterface(new ClipBoardUtils(),"CBUtils");
		webview.addJavascriptInterface(new VibratorUtils(),"VibratorUtils");
		webview.addJavascriptInterface(new IntentUitls(),"IntentUitls");
		webview.addJavascriptInterface(new PermissionUtils(),"PermissionUtils");
	}
	
	public static WebView getWebview(){
		return WebViewUtils.webview;
	}
	
	public static void runJavaScript(String code,String err){
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			webview.evaluateJavascript("try{"+code+"}catch(e){"+err+"}", null);
		} else {
			webview.loadUrl("javascript:try{"+code+"}catch(e){"+err+"}");
		}
	}
	
	public static void runJavaScript(String code){
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			webview.evaluateJavascript("try{"+code+"}catch(e){}", null);
		} else {
			webview.loadUrl("javascript:try{"+code+"}catch(e){}");
		}
	}
}
