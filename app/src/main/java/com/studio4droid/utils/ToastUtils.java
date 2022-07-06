package com.studio4droid.utils;

import android.widget.*;
import android.content.*;
import android.view.*;
import android.app.*;
import android.webkit.JavascriptInterface;
import com.studio4droid.R;

public class ToastUtils {

    private static Toast currentToast;
	private static Context context;
	
	public static void init(Context context){
		ToastUtils.context = context;
	}
	
	@JavascriptInterface
    public static void showShort(String message){
        if(currentToast != null) currentToast.cancel();
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.root));
        TextView text = (TextView) layout.findViewById(R.id.message);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        currentToast = toast;
    }
	
	@JavascriptInterface
    public static void showLong(String message){
        if(currentToast != null) currentToast.cancel();
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.root));
        TextView text = (TextView) layout.findViewById(R.id.message);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        currentToast = toast;
    }
	
	@JavascriptInterface
    public static void cancel(){
        if(currentToast != null){
			currentToast.cancel();
		}
    }
}
