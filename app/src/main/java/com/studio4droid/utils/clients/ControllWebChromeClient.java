package com.studio4droid.utils.clients;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import com.studio4droid.utils.WebViewUtils;

public class ControllWebChromeClient extends WebChromeClient
{
	private static Context context;
	
	public ControllWebChromeClient(Context context){
		ControllWebChromeClient.context = context;
	}
	
	@Override
	public void onProgressChanged(WebView view, int newProgress)
	{
		super.onProgressChanged(view, newProgress);
		WebViewUtils.runJavaScript("onProgress("+newProgress+");");
		if(newProgress == 100){
			WebViewUtils.runJavaScript("onCreate();");
			WebViewUtils.runJavaScript("onStart();");
		}
	}
	
	@Override
	public boolean onJsTimeout()
	{
		return super.onJsTimeout();
	}

	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		super.onConsoleMessage(message, lineNumber, sourceID);
		WebViewUtils.runJavaScript("onConsole('" + message + "'," + lineNumber + ",'" + sourceID + "');");
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message, final JsResult result)
	{
		AlertDialog.Builder dia = new AlertDialog.Builder(context);
		dia.setTitle("Alert");
		dia.setMessage(message);
		dia.setCancelable(true);
		dia.setIcon(android.R.drawable.ic_dialog_alert);
		dia.setPositiveButton("Close", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					result.confirm();
				}
			});
		dia.setOnCancelListener(new DialogInterface.OnCancelListener(){
			public void onCancel(DialogInterface dialog){
				result.cancel();
			}
		});
		dia.show();
		return true;
	}
	
	@Override
	public boolean onJsConfirm(WebView view, String url, String message,final JsResult result)
	{
		AlertDialog.Builder dia = new AlertDialog.Builder(context);
		dia.setTitle("Confirm");
		dia.setMessage(message);
		dia.setCancelable(true);
		dia.setIcon(android.R.drawable.ic_dialog_alert);
		dia.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					result.confirm();
				}
			});
		dia.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					result.cancel();
				}
			});
		dia.setOnCancelListener(new DialogInterface.OnCancelListener(){
			public void onCancel(DialogInterface dialog){
				result.cancel();
			}
		});
		dia.show();
		return true;
	}
	
	@Override
	public boolean onJsPrompt(final WebView view,final String url,final String message,final String defaultValue, final JsPromptResult result)
	{
		AlertDialog.Builder dia = new AlertDialog.Builder(context);
		dia.setTitle("Prompt!");
		dia.setMessage(message);
		dia.setCancelable(true);
		LayoutParams prams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		final EditText et = new EditText(context);
		et.setText(defaultValue);
		et.setLayoutParams(prams);
		dia.setView(et);
		dia.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					result.confirm(et.getText().toString());
				}
			});
		dia.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					result.cancel();
				}
			});
		dia.setOnCancelListener(new DialogInterface.OnCancelListener(){
				public void onCancel(DialogInterface dialog){
					result.cancel();
				}
			});
		dia.show();
		return true;
	}
}
