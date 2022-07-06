package com.studio4droid.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.net.Uri;

public class IntentUitls {

    private static Context context;

	public static void init(Context context){
		IntentUitls.context = context;
	}

	@JavascriptInterface
	public static void facebook(String link){
		try {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + link)));
		} catch (Exception e) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + link)));
		}
	}

	@JavascriptInterface
	public static void youtube(String video){
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+video)));
	}

	@JavascriptInterface
	public static void update(){
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+context.getPackageName())));
	}

	@JavascriptInterface
	public static void rate(){
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+context.getPackageName())));
	}

	@JavascriptInterface
	public static void share(){
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = shareBody = "Application Link : https://play.google.com/store/apps/details?id="+context.getPackageName();
		String shareSub = "Share App Link";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
	}

	@JavascriptInterface
	public static void report(){
		Intent reportEmail = new Intent(Intent.ACTION_SENDTO);
		reportEmail.putExtra(Intent.EXTRA_SUBJECT, "Report");
		reportEmail.setData(Uri.parse("mailto:jahidsite0@gmail.com"));
		reportEmail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(reportEmail);
	}

	@JavascriptInterface
	public static void feedback(){
		Intent feedbackEmail = new Intent(Intent.ACTION_SENDTO);
		feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
		feedbackEmail.setData(Uri.parse("mailto:jahidsite0@gmail.com"));
		feedbackEmail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(feedbackEmail);
	}

}
