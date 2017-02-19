package com.wtlib.base.memcached;

/**
 * ClassName: MemcacheAdapter
 * 
 * @Description: 统一管理缓存配置,按域的方式存储，防止存储不同对象时key重复
 * @author dapengniao
 * @date 2016年7月4日 上午11:25:41
 */
public enum MemcacheAdapter {
	// 集中管理memcached的key前缀，keyPrefix值都用一个字符表示，减少memcached存储体积,864000秒等于10天,超过30天Memcache就自动转换成绝对时间

	// 存放登入cookie和userId的键值对
	LOGIN_COOKIE("㊣", 0, "weidian"), MemcacheDemo("Demo", 864000, "weidian");

	private String nameSpace;
	private String cacheServerId;
	private int expiry;

	/**
	 * @param nameSpace
	 * @param expiry
	 *            单位秒,超过30天Memcache就自动转换成绝对时间
	 * @param cacheServerId
	 *            缓存服务器id，用于标示存到哪个缓存服务器上
	 */
	private MemcacheAdapter(String nameSpace, int expiry, String cacheServerId) {
		this.nameSpace = nameSpace;
		this.expiry = expiry;
		this.cacheServerId = cacheServerId;
	}

	public String getCacheServerId() {
		return cacheServerId;
	}

	public void setCacheServerId(String cacheServerId) {
		this.cacheServerId = cacheServerId;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public int getExpiry() {
		return expiry;
	}

}
