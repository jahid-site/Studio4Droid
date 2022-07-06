package com.studio4droid.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.webkit.JavascriptInterface;

import java.util.ArrayList;

public class PermissionUtils {
    public final static int PERMISSION_REQUEST = 701;
    
	private static Activity activity;
	
	private static ArrayList<String> permissions;
	
	public static void inti(Activity activity){
		PermissionUtils.activity = activity;
		permissions = new ArrayList<String>();
	}

	@JavascriptInterface
	public static void resetPermissions(){
		permissions.clear();
	}
	
	@JavascriptInterface
	public static void showPermissionsDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> notGrantedPerms = new ArrayList<>();
			for (String p : permissions) {
				if (activity.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED){
					notGrantedPerms.add(p);
				}
			}
			String[] requestPermissions = notGrantedPerms.toArray(new String[0]);
            if (requestPermissions != null && requestPermissions.length > 0){
                activity.requestPermissions(requestPermissions, PermissionUtils.PERMISSION_REQUEST);
			}
		} else {
			openPermissionsActivity();
		}
    }
	
	@JavascriptInterface
	public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
				for (String p : permissions) {
					if (activity.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED){
						openPermissionsActivity();
					}
				}
			}
        }
	}
	
	@SuppressLint("JavascriptInterface")
	@TargetApi(Build.VERSION_CODES.M)
	@JavascriptInterface
	private static void openPermissionsActivity(){
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
		activity.startActivityForResult(intent,PERMISSION_REQUEST);
		String notGrantedPerms = "";
		for (String p : permissions) {
			if (activity.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED){
				String str = p.substring(p.lastIndexOf(".")+1,p.length());
				str = str.substring(str.lastIndexOf("_")+1,str.length());
				notGrantedPerms += " " + str.toUpperCase() + " ";
			}
		}
		ToastUtils.showLong("Please Allow permission!\n" + notGrantedPerms);
	}
	
	public static void onRequestPermissionsResult(int requestCode) {
		if (requestCode == PERMISSION_REQUEST) {
            showPermissionsDialog();
        }
	}
}
