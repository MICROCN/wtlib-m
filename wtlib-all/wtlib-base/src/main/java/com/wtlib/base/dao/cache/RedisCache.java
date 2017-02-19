package com.wtlib.base.dao.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import com.wtlib.base.constants.GlobalConstants;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

/**
 * ClassName: RedisCache
 * 
 * @Description: 缓存的工具类
 * @author dapengniao
 * @date 2016年7月4日 下午2:56:41
 */
public class RedisCache implements Cache {
	private static Log logger = LogFactory.getLog(RedisCache.class);
	private static final String REDIS_SERVERS = GlobalConstants
			.getString("redis.servers");
	private static final Integer TIME_BETWEEN_EVICTION_RUNS_MILLIS = GlobalConstants
			.getInt("redis.timeBetweenEvictionRunsMillis");
	private static final Long SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = GlobalConstants
			.getLong("redis.softMinEvictableIdleTimeMillis");
	private static final Long MIN_EVICTABLE_IDLE_TIME_MILLIS = GlobalConstants
			.getLong("redis.minEvictableIdleTimeMillis");
	private static final Long MAX_WAIT_MILLIS = GlobalConstants
			.getLong("redis.maxWaitMillis");
	private static final Integer MAX_TOTAL = GlobalConstants
			.getInt("redis.maxTotal");
	private static final Boolean TEST_ON_BORROW = GlobalConstants
			.getBoolean("redis.testOnBorrow");
	private static final Boolean INIT_REDIS_ON_START_APP = GlobalConstants
			.getBoolean("init.redis.on.start.app");
	private static final Integer TIMEOUT = GlobalConstants
			.getInt("redis.timeout");
	private static final String PASSWORD = GlobalConstants
			.getString("redis.password");
	//
	// private static final String REDIS_SERVERS = "192.168.0.100:6379";
	// private static final Integer TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1;
	// private static final Long SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = (long)
	// 1800000;
	// private static final Long MIN_EVICTABLE_IDLE_TIME_MILLIS = (long)
	// 1800000;
	// private static final Long MAX_WAIT_MILLIS = (long) 10000;
	// private static final Integer MAX_TOTAL = 100;
	// private static final Boolean TEST_ON_BORROW = true;
	// private static final Boolean INIT_REDIS_ON_START_APP = true;
	// private static final Integer TIMEOUT = 3000;
	// private static final String PASSWORD = "weidwtlib";

	private static List<JedisShardInfo> SHARDS = new ArrayList<JedisShardInfo>();
	private static JedisPoolConfig CONF = new JedisPoolConfig();
	// 这里可以根据不同的构造方法去构造链接
	JedisPool pool;
	/** The ReadWriteLock. */
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private String id;
	static {
		if (INIT_REDIS_ON_START_APP != null && INIT_REDIS_ON_START_APP) {
			init();
		}
	}

	private static void init() {
		if (!SHARDS.isEmpty()) {
			return;
		}
		synchronized (SHARDS) {
			if (SHARDS.isEmpty()) {
				CONF.setBlockWhenExhausted(false);
				// 最大连接数, 默认8个,这个值取决于网卡性能的高低，高性能网卡设置高点，反之设置低点
				CONF.setMaxTotal(MAX_TOTAL);
				// 最大空闲连接数, 默认8个
				CONF.setMaxIdle(MAX_TOTAL);
				// 最小空闲连接数, 默认0、推荐0
				CONF.setMinIdle(0);
				// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常,
				// 小于零:阻塞不确定的时间,
				// 默认-1
				CONF.setMaxWaitMillis(MAX_WAIT_MILLIS);
				// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
				CONF.setMinEvictableIdleTimeMillis(MIN_EVICTABLE_IDLE_TIME_MILLIS);
				// 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数
				// 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
				CONF.setSoftMinEvictableIdleTimeMillis(SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
				// 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
				CONF.setTimeBetweenEvictionRunsMillis(TIME_BETWEEN_EVICTION_RUNS_MILLIS);
				// 在引入一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
				CONF.setTestOnBorrow(TEST_ON_BORROW);
				SHARDS = new ArrayList<JedisShardInfo>();
				String[] servers = REDIS_SERVERS.split(",");
				for (String server : servers) {
					String[] ipAndPort = server.split(":");
					SHARDS.add(new JedisShardInfo(ipAndPort[0], Integer
							.valueOf(ipAndPort[1])));
				}
			}
		}
	}

	public RedisCache(final String id) {

		try {
			if (pool == null || pool.isClosed()) {
				synchronized (CONF) {
					if (pool == null || pool.isClosed()) {
						init();
						pool = new JedisPool(CONF, SHARDS.get(0).getHost(),
								SHARDS.get(0).getPort(), TIMEOUT, PASSWORD);
						System.out
								.println("======================================ok");
					}
				}
			}
		} catch (Exception e) {
			if (pool != null) {
				pool.close();
				pool.destroy();
			}
			logger.error(e.getMessage(), e);
		}
		if (id == null) {
			throw new IllegalArgumentException("Cache instances require an ID");
		}
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>MybatisRedisCache:id=" + id);
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	@SuppressWarnings("deprecation")
	public int getSize() {
		Jedis jedis = pool.getResource();
		try {
			return Integer.valueOf(jedis.dbSize().toString());
		} finally {
			if (pool != null && jedis != null) {
				pool.returnResource(jedis);
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void putObject(Object key, Object value) {
		Jedis jedis = pool.getResource();
		try {
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>putObject:" + key + "="
					+ value);
			jedis.set(SerializeUtil.serialize(key.toString()),
					SerializeUtil.serialize(value));
		} finally {
			if (pool != null && jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public Object getObject(Object key) {
		Jedis jedis = pool.getResource();
		try {
			Object value = SerializeUtil.unserialize(jedis.get(SerializeUtil
					.serialize(key.toString())));
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>getObject:" + key + "="
					+ value);
			return value;
		} finally {
			if (pool != null && jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public Object removeObject(Object key) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.expire(SerializeUtil.serialize(key.toString()), 0);
		} finally {
			if (pool != null && jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void clear() {
		Jedis jedis = pool.getResource();
		try {
			jedis.flushDB();
		} finally {
			if (pool != null && jedis != null) {
				pool.returnResource(jedis);
			}
		}
	}

	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

}
