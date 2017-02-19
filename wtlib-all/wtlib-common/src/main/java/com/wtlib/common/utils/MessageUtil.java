package com.wtlib.common.utils;

/**
 * 短信工具类
 * 
 * @className: MessageUtil.java
 * @date 2016年2月1日 下午12:48:07
 */
public class MessageUtil {
	/**
	 * 生成6位的随机数
	 * 
	 * @return
	 */
	public static String generateVerifyNum() {
		return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
	}

	/**
	 * @return 一个当前时间毫秒数的long型数字(暂时用下)
	 */
	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

}
