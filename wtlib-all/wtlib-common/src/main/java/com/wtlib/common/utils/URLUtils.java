package com.wtlib.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class URLUtils {
	public static String getProductCodeInUrl(String url) {
		String matcher = getMatcher("-\\d+"
				+ "\\.", url).trim();

		if (StringUtils.isEmpty(matcher)) {
			return "";
		} else {
			String productCode = matcher.substring(matcher.indexOf("-")+1, matcher.indexOf("."));
			return productCode;
		}
	}

	/**
	 * 根据正则表达式返回字符串中红符合的片段
	 * 
	 * @param regex
	 * @param source
	 * @return
	 */
	public static String getMatcher(String regex, String source) {
		String result = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			result = matcher.group(0);
		}
		return result;
	}

	public static void main(String[] args) {
		String test = "http://www.hznzcn.com/product-29999931492.html";
		String productCodeInUrl = getProductCodeInUrl(test);
		System.out.println(productCodeInUrl);
	}
}
