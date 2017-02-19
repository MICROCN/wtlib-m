package com.wtlib.base.redis;

public enum RedisAdapter {
	IP_LOCATION("a", "CHUAN"), LATEST_MESSAGE("b", "lis"), USER("c",
			"USER_serId");

	private String cacheName;
	private String nameSpace;

	private RedisAdapter(String nameSpace, String cacheName) {
		this.nameSpace = nameSpace;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

}
