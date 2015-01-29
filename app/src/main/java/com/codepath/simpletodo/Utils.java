package com.codepath.simpletodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	public static final String APP_DATE_FORMAT = "yyyy-MM-dd HH:mm a";

	public static boolean IsNullOrEmpty(Object obj) {
		return obj == null || "".equals(obj);
	}

	public static Date ParseDate(String text) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat(APP_DATE_FORMAT);
		return sdf.parse(text);
	}

	public static String ToAppDateFormat(Date date) {
		if (date == null) { date = new Date(); }
		final SimpleDateFormat sdf = new SimpleDateFormat(APP_DATE_FORMAT);
		return sdf.format(date);
	}
}
