package com.codepath.chirrupy.misc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.text.format.DateUtils;

import com.codepath.chirrupy.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Helpers {
	public static String LOG_TAG = "Chirrupy";

	public static String GetRelativeTimeAgo(String rawJsonDate) {
		final String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		
		final SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			
			final CharSequence chars = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(),
					DateUtils.SECOND_IN_MILLIS);
			
			relativeDate = chars.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return relativeDate;
	}

	public static Boolean IsNetworkAvailable(Activity activity) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}

	public Boolean IsOnline() {
		boolean online = false;
		try {
			final Process p1 = Runtime.getRuntime().exec("ping -n 1 www.google.com");
			online = p1.waitFor() == 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return online;
	}

	public static void StyleActionBar(ActionBar actionBar) {
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ACEE")));
		actionBar.setIcon(R.drawable.icon_twitter);
	}
}
