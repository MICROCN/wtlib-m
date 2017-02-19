package com.wtlib.common.utils;

public class WTStringUtils {
	public static String underlineToCamel(String str) {
		String pattern[] = str.split("_");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < pattern.length; i++) {
			if (i == 0) {
				builder.append(pattern[i]);
			} else {
				builder.append(pattern[i].substring(0, 1).toUpperCase());
				builder.append(pattern[i].substring(1));
			}
		}
		return builder.toString();
	}

}
