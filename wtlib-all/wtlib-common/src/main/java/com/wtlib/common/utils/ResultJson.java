package com.wtlib.common.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 返回结果
 * 
 */
public class ResultJson implements Map<String, Object> {

	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 返回成功提示
	 * 
	 * @param code
	 * @return
	 */
	public static ResultJson getSuccessResult(String code, Object msgs) {
		ResultJson map = new ResultJson();
		map.put("status", "success");
		map.put("code", code);
		map.put("message", msgs);
		return map;
	}

	/**
	 * 返回成功提示
	 * 
	 * @param code
	 * @return
	 */
	public static ResultJson getSuccessResult(String code) {
		ResultJson map = new ResultJson();
		map.put("status", "success");
		map.put("code", code);
		return map;
	}
	
	
	public static ResultJson getSuccessResult(Object object) {
		ResultJson map = new ResultJson();
		map.put("status", "success");
		map.put("object", object);
		return map;
	}

	/**
	 * 返回错误提示
	 * 
	 * @param errorCode
	 * @return
	 */
	public static ResultJson getFailResult(String errorCode, Object errorMsgs) {
		ResultJson map = new ResultJson();
		map.put("status", "failure");
		map.put("code", errorCode);
		map.put("message", errorMsgs);
		return map;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return map.entrySet();
	}
}
