package com.wtlib.common.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

	public static void addRefreshCookie(HttpServletResponse response, String name) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Cookie cookie = new Cookie(name, formatter.format(new Date()));
		cookie.setPath("/");
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
	}

	public static String getValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		String value = null;
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				value = cookie.getValue();
				break;
			}
		}

		return value;
	}
}
