package com.codepath.simplechat;

import android.graphics.Color;

import java.util.Random;

public class Utils {
	public static boolean isEmpty(String input) {
		return input == null || input.length() == 0;
	}
	
	public static boolean isBlank(String input) {
		int length;
		if (input == null || (length = input.length()) == 0) { return true; }
		
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(input.charAt(i))) {
				return false;
			}
		}
		
		return true;
	}

	public static int GetRandomColor() {
		final Random rnd = new Random();
		return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
	}
}
