package com.wtlib.base.memcached;

/**
 * ClassName: MemcacheDemo
 * 
 * @Description: Memcache操作实例
 * @author dapengniao
 * @date 2016年7月4日 下午1:24:42
 */
public class MemcacheDemo {
	public static MemCacheClient MemcacheDemo = new MemCacheClient(
			MemcacheAdapter.MemcacheDemo);

	/**
	 * @Description: TODO
	 * @param
	 * @author dapengniao
	 * @date 2016年7月4日 下午1:25:09
	 */
	public static void Demo() {
//		MemcacheDemo.set("timestamp", "timestamp");

		System.out.println("===================="+MemcacheDemo.get("timestamp"));
		
		System.out.println("==================="+MemcacheDemo.getKeyPrefix());
	}

}
