package com.studio4droid.utils;

import android.content.Context;
import android.os.Vibrator;
import android.os.Build;
import android.os.VibrationEffect;
import android.webkit.JavascriptInterface;

public class VibratorUtils {
    
	private static Vibrator vibrator;
	
    public static void init(Context context){
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
    
	@JavascriptInterface
	public static void vibrate(String time){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			vibrator.vibrate(VibrationEffect.createOneShot(Integer.parseInt(time), VibrationEffect.DEFAULT_AMPLITUDE));
		} else {
			vibrator.vibrate(Integer.parseInt(time));
		}
	}
	
	@JavascriptInterface
	public static void cancel(){
		vibrator.cancel();
	}
}
