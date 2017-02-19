package com.wtlib.base.redis;

/**
 * ClassName: RedisDemo
 * @Description: Redis使用示例
 * @author dapengniao
 * @date 2016年7月4日 下午4:58:42
 */
public class RedisDemo {
	public static void Demo() throws Exception {
		RedisClient DemoClient=new RedisClient(RedisAdapter.USER);
		DemoClient.set("xiaocui", "大鹏鸟");
		System.out.println(DemoClient.getStr("xiaocui"));

	}
}
