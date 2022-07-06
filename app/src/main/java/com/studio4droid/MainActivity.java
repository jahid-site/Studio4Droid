package com.studio4droid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.studio4droid.utils.DBUtils;
import com.studio4droid.utils.PermissionUtils;
import com.studio4droid.utils.WebViewUtils;

public class MainActivity extends Activity {
	public final String HOME_URL = "file:///storage/emulated/0/Apps/www/index.html";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		DBUtils.init(this);
		switch(DBUtils.getData("theme","light")){
			case "light":
				setTheme(R.style.AppTheme);
				break;
			case "dark":
				setTheme(R.style.AppThemeDark);
				break;
			case "default":
				setTheme(R.style.AppThemeDefault);
				break;
			default:
			   break;
		}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		WebViewUtils.inti(this);
		WebViewUtils.getWebview().loadUrl(HOME_URL);
    }
	
	@Override
	public void onBackPressed() {
		WebViewUtils.runJavaScript("onBackPressed();");
	}

	@Override
	protected void onStart() {
		super.onStart();
		WebViewUtils.runJavaScript("onStart();");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		WebViewUtils.runJavaScript("onRestart();");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		WebViewUtils.runJavaScript("onResume();");
	}

	@Override
	protected void onPause() {
		super.onPause();
		WebViewUtils.runJavaScript("onPause();");
	}

	@Override
	protected void onStop() {
		super.onStop();
		WebViewUtils.runJavaScript("onStop();");
	}

	@Override
	protected void onDestroy() {
		WebViewUtils.runJavaScript("onDestroy();");
		super.onDestroy();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		WebViewUtils.runJavaScript("onKeyUp("+keyCode+");");
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		WebViewUtils.runJavaScript("onKeyDown("+keyCode+");");
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		PermissionUtils.onRequestPermissionsResult(requestCode);
	}
	
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		PermissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
