package com.studio4droid.utils.clients;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Intent;
import android.net.Uri;

public class ControllWebViewClient extends WebViewClient
{
	private Activity activity;
	
	public ControllWebViewClient(Activity activity){
		this.activity = activity;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url)
	{
		if (url.startsWith("file:")) {
			return false;
		}
		activity.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
		return true;
	}
}
