package com.wtlib.base.constants;

import java.util.Properties;

public class GlobalConstants {

	public static Properties interfaceUrlProperties;

/**
 * 
 * @Description: TODO
 * @param @param key
 * @param @return   
 * @author dapengniao
 * @date 2015年10月13日 下午4:59:14
 */
	public static String getString(String key) {
		String Properties=(String) interfaceUrlProperties.get(key);
		return Properties == null ? null : Properties;
	}
	
	
	public static Integer getInt(String key) {
		String Properties=(String) interfaceUrlProperties.get(key);
		return Properties == null ? null : Integer.parseInt(Properties);
	}
	
	public static Boolean getBoolean(String key) {
		String Properties=(String) interfaceUrlProperties.get(key);
		return Properties == null ? null :Boolean.valueOf(Properties);
	}
		
	
	public static Long getLong(String key) {
		String Properties=(String) interfaceUrlProperties.get(key);
		return Properties == null ? null :Long.valueOf(Properties);
	}
	
}
