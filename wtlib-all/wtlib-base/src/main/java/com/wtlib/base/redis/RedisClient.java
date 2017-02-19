package com.wtlib.base.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

import com.wtlib.base.constants.GlobalConstants;

public class RedisClient {
	protected final static Logger logger = LoggerFactory.getLogger(RedisClient.class);
	private static final String REDIS_SERVERS = GlobalConstants.getString("redis.servers");
	private static final Integer TIME_BETWEEN_EVICTION_RUNS_MILLIS = GlobalConstants.getInt("redis.timeBetweenEvictionRunsMillis");
	private static final Long SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = GlobalConstants.getLong("redis.softMinEvictableIdleTimeMillis");
	private static final Long MIN_EVICTABLE_IDLE_TIME_MILLIS = GlobalConstants.getLong("redis.minEvictableIdleTimeMillis");
	private static final Long MAX_WAIT_MILLIS = GlobalConstants.getLong("redis.maxWaitMillis");
	private static final Integer MAX_TOTAL = GlobalConstants.getInt("redis.maxTotal");
	private static final Boolean TEST_ON_BORROW =  GlobalConstants.getBoolean("redis.testOnBorrow");
	private static final Boolean INIT_REDIS_ON_START_APP = GlobalConstants.getBoolean("init.redis.on.start.app");
	private static final Integer TIMEOUT = GlobalConstants.getInt("redis.timeout");
	private static final String PASSWORD = GlobalConstants.getString("redis.password");
	private static List<JedisShardInfo> SHARDS = new ArrayList<JedisShardInfo>();
	private static JedisPoolConfig CONF = new JedisPoolConfig();
	private static JedisPool JEDIS_POOL;
	private String nameSpace;
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
					SHARDS.add(new JedisShardInfo(ipAndPort[0], Integer.valueOf(ipAndPort[1])));
				}
			}
		}
	}

	public RedisClient(RedisAdapter redisAdapter) {
		try {
			this.nameSpace = redisAdapter.getNameSpace();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	public static JedisPool getJedisPool() {
		try {
			if (JEDIS_POOL == null || JEDIS_POOL.isClosed()) {
				synchronized (CONF) {
					if (JEDIS_POOL == null || JEDIS_POOL.isClosed()) {
						init();

						JEDIS_POOL = new JedisPool(CONF, SHARDS.get(0).getHost(), SHARDS.get(0).getPort(), TIMEOUT,
								PASSWORD);
					}
				}
			}
		} catch (Exception e) {
			if (JEDIS_POOL != null) {
				JEDIS_POOL.close();
				JEDIS_POOL.destroy();
			}
			logger.error(e.getMessage(),e);
		}
		return JEDIS_POOL;
	}

	
	/**
	 * 给key这个键递增amount(如果为负数则是递减amount)，并返回递增或递减后的值，如果原来没有值则以0作为原有值,
	 * incr的值可以通过getStr和incr自身获得但不能通过getByte获得,也不能对set进去的值做incr
	 *
	 * @param key
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public Long incr(Object key, long amount) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long newValue;
		try {
			newValue = jedis.incrBy(generateKey(key), amount);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return newValue;
	}

	/**
	 * 将 key 中储存的数字值增一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR
	 * 操作。,incr的值可以通过getStr和incr自身获得但不能通过getByte获得,也不能对set进去的值做incr
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 *
	 * @param key
	 * @return 执行 INCR 命令之后 key 的值
	 * @throws Exception
	 */
	public Long incr(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long newValue;
		try {
			newValue = jedis.incr(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return newValue;
	}

	/**
	 * 给key这个键递减1，并返回递减后的值，如果原来没有值则以0作为原有值
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Long decr(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long newValue;
		try {
			newValue = jedis.decr(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return newValue;
	}

	/**
	 * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
	 * 对于某个原本带有生存时间（TTL）的键来说， 当 SET 命令成功在这个键上执行时， 这个键原有的 TTL 将被清除
	 *
	 * @param key
	 * @param value
	 * @return 操作成功完成时，才返回 OK 。
	 * @throws Exception
	 */
	public String set(Object key, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String resultCode;
		try {
			resultCode = jedis.set(generateKey(key), value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return resultCode;
	}

	/**
	 * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
	 * 对于某个原本带有生存时间（TTL）的键来说， 当 SET 命令成功在这个键上执行时， 这个键原有的 TTL 将被清除
	 *
	 * @param key
	 * @param value
	 * @return 操作成功完成时，才返回 OK 。
	 * @throws Exception
	 */
	public String set(Object key, byte[] value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String resultCode;
		try {
			resultCode = jedis.set(generateKey(key).getBytes(), value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return resultCode;
	}

	/**
	 * 获取主键为key的值，返回字符串类型值
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getStr(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String value;
		try {
			value = jedis.get(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 获取主键为key的值，返回字符串类型值
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public byte[] getBytes(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		byte[] value;
		try {
			value = jedis.get(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 检查给定 key 是否存在。
	 *
	 * @param key
	 * @return 若 key 存在，返回 1 ，否则返回 0 。
	 * @throws Exception
	 */
	public Boolean exists(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Boolean exists;
		try {
			exists = jedis.exists(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return exists;
	}

	/**
	 * 删除给定的一个或多个 key 。 不存在的 key 会被忽略。
	 *
	 * @param key
	 * @return 被删除 key 的数量。
	 * @throws Exception
	 */
	public Long del(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long count;
		try {
			count = jedis.del(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return count;
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除 .生存时间可以通过使用 DEL 命令来删除整个 key
	 * 来移除，或者被 SET 和 GETSET 命令覆写(overwrite)，这意味着，如果一个命令只是修改(alter)一个带生存时间的 key
	 * 的值而不是用一个新的 key 值来代替(replace)它的话，那么生存时间不会被改变。 比如说，对一个 key 执行 INCR
	 * 命令，对一个列表进行 LPUSH 命令，或者对一个哈希表执行 HSET 命令，这类操作都不会修改 key 本身的生存时间。 另一方面，如果使用
	 * RENAME 对一个 key 进行改名，那么改名后的 key 的生存时间和改名前一样。 RENAME 命令的另一种可能是，尝试将一个带生存时间的
	 * key 改名成另一个带生存时间的 another_key ，这时旧的 another_key (以及它的生存时间)会被删除，然后旧的 key
	 * 会改名为 another_key ，因此，新的 another_key 的生存时间也和原本的 key 一样。 使用 PERSIST
	 * 命令可以在不删除 key 的情况下，移除 key 的生存时间，让 key 重新成为一个『持久的』(persistent) key 。
	 *
	 * @param key
	 * @param seconds
	 * @return 设置成功返回 1 。 当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的 Redis
	 *         中你尝试更新 key 的生存时间)，返回 0 。
	 * @throws Exception
	 */
	public Long expire(Object key, int seconds) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long status;
		try {
			status = jedis.expire(generateKey(key), seconds);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX
	 * 时间戳(unix timestamp)。
	 *
	 * @param key
	 * @param timestamp
	 * @return 如果生存时间设置成功，返回 1 。 当 key 不存在或没办法设置生存时间，返回 0
	 * @throws Exception
	 */
	public Long expireAt(Object key, long timestamp) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long status;
		try {
			status = jedis.expireAt(generateKey(key), timestamp);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 将当前数据库的 key 移动到给定的数据库 db 当中。 如果当前数据库(源数据库)和给定数据库(目标数据库)有相同名字的给定 key ，或者
	 * key 不存在于当前数据库，那么 MOVE 没有任何效果。 因此，也可以利用这一特性，将 MOVE
	 * 当作锁(locking)原语(primitive)。
	 *
	 * @param key
	 * @param dbIndex
	 * @return 移动成功返回 1 ，失败则返回 0 。
	 * @throws Exception
	 */
	public Long move(Object key, int dbIndex) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long status;
		try {
			status = jedis.move(generateKey(key), dbIndex);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )。
	 *
	 * @param key
	 * @return 当生存时间移除成功时，返回 1 . 如果 key 不存在或 key 没有设置生存时间，返回 0
	 * @throws Exception
	 */
	public Long persist(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long status;
		try {
			status = jedis.persist(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
	 *
	 * @param key
	 * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key
	 *         的剩余生存时间。
	 * @throws Exception
	 */
	public Long ttl(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long status;
		try {
			status = jedis.ttl(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 返回键值从小到大排序的结果。
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public List<String> sort(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		List<String> result;
		try {
			result = jedis.sort(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * SORT 命令默认排序对象为数字， 当需要对字符串进行排序时， 需要显式地在 SORT 命令之后添加 ALPHA 修饰符
	 * 排序之后返回元素的数量可以通过 LIMIT 修饰符进行限制， 修饰符接受 offset 和 count 两个参数： •offset
	 * 指定要跳过的元素数量。 •count 指定跳过 offset 个指定的元素之后，要返回多少个对象。
	 *
	 * @param key
	 * @param sortingParameters
	 * @return
	 * @throws Exception
	 */
	public List<String> sort(Object key, SortingParams sortingParameters) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		List<String> result;
		try {
			result = jedis.sort(generateKey(key), sortingParameters);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。 如果 key 不存在， APPEND
	 * 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
	 *
	 * @param key
	 * @param value
	 * @return 追加 value 之后， key 中字符串的长度。
	 * @throws Exception
	 */
	public Long append(Object key, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long length;
		try {
			length = jedis.append(generateKey(key), value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return length;
	}

	/**
	 * 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。
	 * 负数偏移量表示从字符串最后开始计数， -1 表示最后一个字符， -2 表示倒数第二个，以此类推。 GETRANGE
	 * 通过保证子字符串的值域(range)不超过实际字符串的值域来处理超出范围的值域请求。
	 *
	 * @param key
	 * @param startOffset
	 * @param endOffset
	 * @return
	 * @throws Exception
	 */
	public String getrange(Object key, long startOffset, long endOffset) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String subStr;
		try {
			subStr = jedis.getrange(generateKey(key), startOffset, endOffset);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return subStr;
	}

	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。 当 key 存在但不是字符串类型时，返回一个错误
	 *
	 * @param key
	 * @param value
	 * @return 返回给定 key 的旧值。 当 key 没有旧值时，也即是， key 不存在时，返回 nil 。
	 * @throws Exception
	 */
	public String getSet(Object key, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String oldValue;
		try {
			oldValue = jedis.getSet(generateKey(key), value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return oldValue;
	}

	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
	 *
	 * @param key
	 * @param value
	 * @return 返回给定 key 的旧值。 当 key 没有旧值时，也即是， key 不存在时，返回 nil 。
	 * @throws Exception
	 */
	public byte[] getSet(Object key, byte[] value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		byte[] oldValue;
		try {
			oldValue = jedis.getSet(generateKey(key).getBytes(), value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return oldValue;
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。 如果 key 已经存在， SETEX
	 * 命令将覆写旧值。 这个命令类似于以下两个命令： SET key value、 EXPIRE key seconds # 设置生存时间 不同之处是，
	 * SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用。
	 *
	 * @param key
	 * @param seconds
	 *            有效期，单位秒
	 * @param value
	 * @return 设置成功时返回 OK 。 当 seconds 参数不合法时，返回一个错误。
	 * @throws Exception
	 */
	public String setex(Object key, int seconds, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String status;
		try {
			status = jedis.setex(generateKey(key), seconds, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。 如果 key 已经存在， SETEX
	 * 命令将覆写旧值。 这个命令类似于以下两个命令： SET key value、 EXPIRE key seconds # 设置生存时间 不同之处是，
	 * SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用。
	 *
	 * @param key
	 * @param seconds
	 *            有效期，单位秒
	 * @param value
	 * @return 设置成功时返回 OK 。 当 seconds 参数不合法时，返回一个错误。
	 * @throws Exception
	 */
	public String setex(Object key, int seconds, byte[] value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String status;
		try {
			status = jedis.setex(generateKey(key).getBytes(), seconds, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始。 不存在的 key
	 * 当作空白字符串处理。 SETRANGE 命令会确保字符串足够长以便将 value 设置在指定的偏移量上，如果给定 key
	 * 原来储存的字符串长度比偏移量小(比如字符串只有 5 个字符长，但你设置的 offset 是 10
	 * )，那么原字符和偏移量之间的空白将用零字节(zerobytes, "\x00" )来填充。 注意你能使用的最大偏移量是
	 * 2^29-1(536870911) ，因为 Redis 字符串的大小被限制在 512
	 * 兆(megabytes)以内。如果你需要使用比这更大的空间，你可以使用多个 key
	 *
	 * @param key
	 * @param offset
	 * @param value
	 * @return 被 SETRANGE 修改之后，字符串的长度
	 * @throws Exception
	 */
	public Long setrange(Object key, long offset, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long length;
		try {
			length = jedis.setrange(generateKey(key), offset, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return length;
	}

	/**
	 * 返回 key 所储存的字符串值的长度。 当 key 储存的不是字符串值时，返回一个错误。
	 *
	 * @param key
	 * @return 字符串值的长度。 当 key 不存在时，返回 0 。
	 * @throws Exception
	 */
	public Long strlen(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long length;
		try {
			length = jedis.strlen(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return length;
	}

	/**
	 * 给主键为key的比特字符串（类似110011）的offset位设置1或者0，如果key不存在值新建一个，返回这个key的offset偏移量原来的值
	 *
	 * @param key
	 * @param offset
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public Boolean setbit(Object key, long offset, boolean value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Boolean oldValue;
		try {
			oldValue = jedis.setbit(generateKey(key), offset, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return oldValue;
	}

	/**
	 * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。 当 offset 比字符串值的长度大，或者 key 不存在时，返回 0
	 *
	 * @param key
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	public Boolean getbit(Object key, long offset) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Boolean bitValue;
		try {
			bitValue = jedis.getbit(generateKey(key), offset);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return bitValue;
	}

	/**
	 * 统计比特字符串（类似1010100）中位为1的数量
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Long bitcount(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long oldValue;
		try {
			oldValue = jedis.bitcount(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return oldValue;
	}

	/**
	 * 统计比特字符串（类似1010100）中从偏移量start到end之间位为1的数量
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public Long bitcount(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long oldValue;
		try {
			oldValue = jedis.bitcount(generateKey(key), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return oldValue;
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。 如果有多个 value 值，那么各个 value
	 * 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c
	 * ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c 。 如果 key
	 * 不存在，一个空列表会被创建并执行 RPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误
	 *
	 * @param key
	 * @param strings
	 * @return 执行 RPUSH 操作后，表的长度
	 * @throws Exception
	 */
	public Long rpush(Object key, String... strings) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.rpush(generateKey(key), strings);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。 如果有多个 value 值，那么各个 value
	 * 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c
	 * ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c 。 如果 key
	 * 不存在，一个空列表会被创建并执行 RPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误
	 *
	 * @param key
	 * @param bytes
	 * @return 执行 RPUSH 操作后，表的长度
	 * @throws Exception
	 */
	public Long rpush(Object key, byte[]... bytes) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.rpush(generateKey(key).getBytes(), bytes);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 将值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。 和 RPUSH 命令相反，当 key 不存在时，
	 * RPUSHX 命令什么也不做。
	 *
	 * @param key
	 * @param strings
	 * @return RPUSHX 命令执行之后，表的长度。
	 * @throws Exception
	 */
	public Long rpushx(Object key, String... strings) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.rpushx(generateKey(key), strings);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 将值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。 和 RPUSH 命令相反，当 key 不存在时，
	 * RPUSHX 命令什么也不做。
	 *
	 * @param key
	 * @param bytes
	 * @return RPUSHX 命令执行之后，表的长度。
	 * @throws Exception
	 */
	public Long rpushx(Object key, byte[]... bytes) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.rpushx(generateKey(key).getBytes(), bytes);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后。 当 pivot 不存在于列表 key 时，不执行任何操作。 当
	 * key 不存在时， key 被视为空列表，不执行任何操作。 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key
	 * @param where
	 * @param pivot
	 * @param value
	 * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到 pivot ，返回 -1 。 如果 key
	 *         不存在或为空列表，返回 0 。
	 * @throws Exception
	 */
	public Long linsert(Object key, LIST_POSITION where, String pivot, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.linsert(generateKey(key), where, pivot, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后。 当 pivot 不存在于列表 key 时，不执行任何操作。 当
	 * key 不存在时， key 被视为空列表，不执行任何操作。 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key
	 * @param where
	 * @param pivot
	 * @param value
	 * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到 pivot ，返回 -1 。 如果 key
	 *         不存在或为空列表，返回 0 。
	 * @throws Exception
	 */
	public Long linsert(Object key, LIST_POSITION where, byte[] pivot, byte[] value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.linsert(generateKey(key).getBytes(), where, pivot, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 返回列表 key 中，下标为 index 的元素。 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0
	 * 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2
	 * 表示列表的倒数第二个元素，以此类推。 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key
	 * @param index
	 * @return 列表中下标为 index 的元素。 如果 index 参数的值不在列表的区间范围内(out of range)，返回 nil 。
	 * @throws Exception
	 */
	public String lindex(Object key, long index) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String value;
		try {
			value = jedis.lindex(generateKey(key), index);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 返回列表 key 中，下标为 index 的元素。 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0
	 * 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2
	 * 表示列表的倒数第二个元素，以此类推。 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key
	 * @param index
	 * @return 列表中下标为 index 的元素。 如果 index 参数的值不在列表的区间范围内(out of range)，返回 nil 。
	 * @throws Exception
	 */
	public byte[] lindexBytes(Object key, long index) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		byte[] value;
		try {
			value = jedis.lindex(generateKey(key).getBytes(), index);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 返回列表 key 的长度。 如果 key 不存在，则 key 被解释为一个空列表，返回 0 . 如果 key 不是列表类型，返回一个错误
	 *
	 * @param key
	 * @return 列表 key 的长度。
	 * @throws Exception
	 */
	public Long llen(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long length;
		try {
			length = jedis.llen(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return length;
	}

	/**
	 * 移除并返回列表 key中的头元素。
	 *
	 * @param key
	 * @param index
	 * @return 列表的头元素。 当 key 不存在时，返回 nil
	 * @throws Exception
	 */
	public String lpop(Object key, long index) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String firstElement;
		try {
			firstElement = jedis.lpop(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return firstElement;
	}

	/**
	 * 移除并返回列表 key中的头元素。
	 *
	 * @param key
	 * @param index
	 * @return 列表的头元素。 当 key 不存在时，返回 nil
	 * @throws Exception
	 */
	public byte[] lpopBytes(Object key, long index) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		byte[] firstElement;
		try {
			firstElement = jedis.lpop(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return firstElement;
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表头 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头：
	 * 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH
	 * mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。 如果 key 不存在，一个空列表会被创建并执行
	 * LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
	 *
	 * @param key
	 * @param strings
	 * @return 执行 LPUSH 命令后，列表的长度
	 * @throws Exception
	 */
	public Long lpush(Object key, String... strings) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long length;
		try {
			length = jedis.lpush(generateKey(key), strings);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return length;
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表头 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头：
	 * 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH
	 * mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。 如果 key 不存在，一个空列表会被创建并执行
	 * LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
	 *
	 * @param key
	 * @param bytes
	 * @return 执行 LPUSH 命令后，列表的长度
	 * @throws Exception
	 */
	public Long lpush(Object key, byte[]... bytes) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long length;
		try {
			length = jedis.lpush(generateKey(key).getBytes(), bytes);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return length;
	}

	/**
	 * 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。 和 LPUSH 命令相反，当 key 不存在时，
	 * LPUSHX 命令什么也不做。
	 *
	 * @param key
	 * @param strings
	 * @return LPUSHX 命令执行之后，表的长度。
	 * @throws Exception
	 */
	public Long lpushx(Object key, String... strings) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long length;
		try {
			length = jedis.lpushx(generateKey(key), strings);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return length;
	}

	/**
	 * 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。 和 LPUSH 命令相反，当 key 不存在时，
	 * LPUSHX 命令什么也不做。
	 *
	 * @param key
	 * @param bytes
	 * @return LPUSHX 命令执行之后，表的长度。
	 * @throws Exception
	 */
	public Long lpushx(Object key, byte[]... bytes) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long length;
		try {
			length = jedis.lpushx(generateKey(key).getBytes(), bytes);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return length;
	}

	/**
	 *
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。 下标(index)参数 start 和 stop 都以 0
	 * 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素，
	 * -2 表示列表的倒数第二个元素，以此类推。 注意LRANGE命令和编程语言区间函数的区别 假如你有一个包含一百个元素的列表，对该列表执行
	 * LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE
	 * 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的
	 * range() 函数。 超出范围的下标 超出范围的下标值不会引起错误。 如果 start 下标比列表的最大下标 end ( LLEN list
	 * 减去 1 )还要大，那么 LRANGE 返回一个空列表。 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end
	 * 。
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return 一个列表，包含指定区间内的元素。
	 * @throws Exception
	 */
	public List<String> lrange(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		List<String> result;
		try {
			result = jedis.lrange(generateKey(key), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 *
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。 下标(index)参数 start 和 stop 都以 0
	 * 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素，
	 * -2 表示列表的倒数第二个元素，以此类推。 注意LRANGE命令和编程语言区间函数的区别 假如你有一个包含一百个元素的列表，对该列表执行
	 * LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE
	 * 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的
	 * range() 函数。 超出范围的下标 超出范围的下标值不会引起错误。 如果 start 下标比列表的最大下标 end ( LLEN list
	 * 减去 1 )还要大，那么 LRANGE 返回一个空列表。 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end
	 * 。
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return 一个列表，包含指定区间内的元素。
	 * @throws Exception
	 */
	public List<byte[]> lrangeByte(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		List<byte[]> result;
		try {
			result = jedis.lrange(generateKey(key).getBytes(), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。 count 的值可以是以下几种： •count > 0 :
	 * 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。 •count < 0 : 从表尾开始向表头搜索，移除与 value
	 * 相等的元素，数量为 count 的绝对值。 •count = 0 : 移除表中所有与 value 相等的值。
	 *
	 * @param key
	 * @param count
	 * @param value
	 * @return 被移除元素的数量。 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回
	 *         0 。
	 * @throws Exception
	 */
	public Long lrem(Object key, long count, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.lrem(generateKey(key), count, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。 count 的值可以是以下几种： •count > 0 :
	 * 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。 •count < 0 : 从表尾开始向表头搜索，移除与 value
	 * 相等的元素，数量为 count 的绝对值。 •count = 0 : 移除表中所有与 value 相等的值。
	 *
	 * @param key
	 * @param count
	 * @param value
	 * @return 被移除元素的数量。 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回
	 *         0 。
	 * @throws Exception
	 */
	public Long lrem(Object key, long count, byte[] value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.lrem(generateKey(key).getBytes(), count, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 将列表 key 下标为 index 的元素的值设置为 value 。 当 index 参数超出范围，或对一个空列表( key 不存在)进行
	 * LSET 时，返回一个错误。
	 *
	 * @param key
	 * @param index
	 * @param value
	 * @return 操作成功返回 ok ，否则返回错误信息。
	 * @throws Exception
	 */
	public String lset(Object key, long index, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String status;
		try {
			status = jedis.lset(generateKey(key), index, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 将列表 key 下标为 index 的元素的值设置为 value 。 当 index 参数超出范围，或对一个空列表( key 不存在)进行
	 * LSET 时，返回一个错误。
	 *
	 * @param key
	 * @param index
	 * @param value
	 * @return 操作成功返回 ok ，否则返回错误信息。
	 * @throws Exception
	 */
	public String lset(Object key, long index, byte[] value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String status;
		try {
			status = jedis.lset(generateKey(key).getBytes(), index, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 举个例子，执行命令 LTRIM list
	 * 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除。 下标(index)参数 start 和 stop 都以 0
	 * 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素，
	 * -2 表示列表的倒数第二个元素，以此类推。 当 key 不是列表类型时，返回一个错误。 LTRIM 命令通常和 LPUSH 命令或 RPUSH
	 * 命令配合使用，举个例子： LPUSH log newest_log LTRIM log 0 99 这个例子模拟了一个日志程序，每次将最新日志
	 * newest_log 放到 log 列表中，并且只保留最新的 100 项。注意当这样使用 LTRIM
	 * 命令时，时间复杂度是O(1)，因为平均情况下，每次只有一个元素被移除。 注意LTRIM命令和编程语言区间函数的区别
	 * 假如你有一个包含一百个元素的列表 list ，对该列表执行 LTRIM list 0 10 ，结果是一个包含11个元素的列表，这表明 stop
	 * 下标也在 LTRIM 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、
	 * Array#slice 和Python的 range() 函数。 超出范围的下标 超出范围的下标值不会引起错误。 如果 start
	 * 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，或者 start > stop ， LTRIM 返回一个空列表(因为
	 * LTRIM 已经将整个列表清空)。 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return 命令执行成功时，返回 ok 。
	 * @throws Exception
	 */
	public String ltrim(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String status;
		try {
			status = jedis.ltrim(generateKey(key), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 移除并返回列表 key中的尾元素。
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String rpop(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String lastElement;
		try {
			lastElement = jedis.rpop(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return lastElement;
	}

	/**
	 * 移除并返回列表 key中的尾元素。
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public byte[] rpopBytes(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		byte[] lastElement;
		try {
			lastElement = jedis.rpop(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return lastElement;
	}

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
	 *
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该
	 * member 在正确的位置上。
	 *
	 * score 值可以是整数值或双精度浮点数。
	 *
	 * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
	 *
	 * 当 key 存在但不是有序集类型时，返回一个错误
	 *
	 * @param key
	 * @param score
	 * @param member
	 * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
	 * @throws Exception
	 */
	public Long zadd(Object key, double score, byte[] member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long addCount;
		try {
			addCount = jedis.zadd(generateKey(key).getBytes(), score, member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return addCount;
	}

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
	 *
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该
	 * member 在正确的位置上。
	 *
	 * score 值可以是整数值或双精度浮点数。
	 *
	 * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
	 *
	 * 当 key 存在但不是有序集类型时，返回一个错误
	 *
	 * @param key
	 * @param score
	 * @param member
	 * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
	 * @throws Exception
	 */
	public Long zadd(Object key, double score, String member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long addCount;
		try {
			addCount = jedis.zadd(generateKey(key), score, member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return addCount;
	}

	/**
	 * 返回有序集 key 的元素个数。
	 *
	 * @param key
	 * @return 当 key 存在且是有序集类型时，返回有序集的基数。
	 *
	 *         当 key 不存在时，返回 0
	 * @throws Exception
	 */
	public Long zcard(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.zcard(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间的成员的数量
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 * @throws Exception
	 */
	public Long zcount(Object key, double min, double max) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.zcount(generateKey(key).getBytes(), min, max);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
	 *
	 * 可以通过传递一个负数值 increment ，让 score 减去相应的值 . 当 key 不存在，或 member 不是 key 的成员时，
	 * ZINCRBY key increment member 等同于 ZADD key increment member 。
	 *
	 * 当 key 不是有序集类型时，返回一个错误
	 *
	 * @param key
	 * @param amount
	 * @param member
	 * @return member 成员的新 score 值，以字符串形式表示
	 * @throws Exception
	 */
	public Double zincrby(Object key, double amount, byte[] member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Double newScore;
		try {
			newScore = jedis.zincrby(generateKey(key).getBytes(), amount, member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return newScore;
	}

	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
	 *
	 * 可以通过传递一个负数值 increment ，让 score 减去相应的值 . 当 key 不存在，或 member 不是 key 的成员时，
	 * ZINCRBY key increment member 等同于 ZADD key increment member 。
	 *
	 * 当 key 不是有序集类型时，返回一个错误
	 *
	 * @param key
	 * @param amount
	 * @param member
	 * @return member 成员的新 score 值，以字符串形式表示
	 * @throws Exception
	 */
	public Double zincrby(Object key, double amount, String member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Double newScore;
		try {
			newScore = jedis.zincrby(generateKey(key), amount, member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return newScore;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。
	 *
	 * 其中成员的位置按 score 值递增(从小到大)来排序。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
	 *
	 *
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 *
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 *
	 *
	 * 超出范围的下标并不会引起错误。
	 *
	 * 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。
	 *
	 * 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return 指定区间内，不带score 值(可选)的有序集成员的列表
	 * @throws Exception
	 */
	public Set<byte[]> zrange(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> members;
		try {
			members = jedis.zrange(generateKey(key).getBytes(), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return members;
	}

	/**
	 *
	 * 返回有序集 key 中，指定区间内的成员和score。
	 *
	 * 其中成员的位置按 score 值递增(从小到大)来排序。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
	 *
	 *
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 *
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 *
	 *
	 * 超出范围的下标并不会引起错误。
	 *
	 * 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。
	 *
	 * 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public Set<Tuple> zrangeWithScores(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<Tuple> memberAndScore;
		try {
			memberAndScore = jedis.zrangeWithScores(generateKey(key).getBytes(), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return memberAndScore;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。
	 *
	 * 其中成员的位置按 score 值递减(从大到小)来排序。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
	 *
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 *
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 *
	 *
	 * 超出范围的下标并不会引起错误。
	 *
	 * 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。
	 *
	 * 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return 指定区间内，不带score 值(可选)的有序集成员的列表
	 * @throws Exception
	 */
	public Set<byte[]> zrevrange(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> members;
		try {
			members = jedis.zrevrange(generateKey(key).getBytes(), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return members;
	}

	/**
	 * 返回字符串型排序结果
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public Set<String> zrevrangeStr(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<String> members;
		try {
			members = jedis.zrevrange(generateKey(key), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return members;
	}

	/**
	 *
	 * 返回有序集 key 中，指定区间内的成员和score。
	 *
	 * 其中成员的位置按score 值递减(从大到小)来排序。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
	 *
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 *
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 *
	 *
	 * 超出范围的下标并不会引起错误。
	 *
	 * 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。
	 *
	 * 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public Set<Tuple> zrevrangeWithScores(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<Tuple> memberAndScore;
		try {
			memberAndScore = jedis.zrevrangeWithScores(generateKey(key).getBytes(), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return memberAndScore;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )成员。有序集成员按 score
	 * 值递增(从小到大)次序排列。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列. min 和 max 可以是 -inf 和 +inf
	 * ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
	 *
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 * @throws Exception
	 */
	public Set<byte[]> zrangeByScore(Object key, double min, double max) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> members;
		try {
			members = jedis.zrangeByScore(generateKey(key).getBytes(), min, max);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return members;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max
	 * )的第offset起的count个成员。有序集成员按 score 值递增(从小到大)次序排列。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列. min 和 max 可以是 -inf 和 +inf
	 * ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
	 *
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public Set<byte[]> zrangeByScore(Object key, double min, double max, int offset, int count) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> members;
		try {
			members = jedis.zrangeByScore(generateKey(key).getBytes(), min, max, offset, count);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return members;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )成员和score。有序集成员按
	 * score 值递增(从小到大)次序排列。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列. min 和 max 可以是 -inf 和 +inf
	 * ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
	 *
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 * @throws Exception
	 */
	public Set<Tuple> zrangeByScoreWithScores(Object key, double min, double max) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<Tuple> memberAndScore;
		try {
			memberAndScore = jedis.zrangeByScoreWithScores(generateKey(key).getBytes(), min, max);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return memberAndScore;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max
	 * )的第offset起的count个成员和score。有序集成员按 score 值递增(从小到大)次序排列。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列. min 和 max 可以是 -inf 和 +inf
	 * ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
	 *
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public Set<Tuple> zrangeByScoreWithScores(Object key, double min, double max, int offset, int count)
			throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<Tuple> memberAndScore;
		try {
			memberAndScore = jedis.zrangeByScoreWithScores(generateKey(key).getBytes(), min, max, offset, count);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return memberAndScore;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )成员。有序集成员按 score
	 * 值递减(从大到小)次序排列。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列. min 和 max 可以是 -inf 和 +inf
	 * ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
	 *
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 * @throws Exception
	 */
	public Set<byte[]> zrevrangeByScore(Object key, double min, double max) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> members;
		try {
			members = jedis.zrevrangeByScore(generateKey(key).getBytes(), max, min);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return members;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max
	 * )的第offset起的count个成员。有序集成员按 score 递减(从大到小)次序排列。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列. min 和 max 可以是 -inf 和 +inf
	 * ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
	 *
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public Set<byte[]> zrevrangeByScore(Object key, double min, double max, int offset, int count) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> members;
		try {
			members = jedis.zrevrangeByScore(generateKey(key).getBytes(), max, min, offset, count);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return members;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )成员和score。有序集成员按
	 * score 递减(从大到小)次序排列。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列. min 和 max 可以是 -inf 和 +inf
	 * ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
	 *
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 * @throws Exception
	 */
	public Set<Tuple> zrevrangeByScoreWithScores(Object key, double min, double max) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<Tuple> memberAndScore;
		try {
			memberAndScore = jedis.zrevrangeByScoreWithScores(generateKey(key).getBytes(), max, min);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return memberAndScore;
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max
	 * )的第offset起的count个成员和score。有序集成员按 score 值递减(从大到小)次序排列。
	 *
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列. min 和 max 可以是 -inf 和 +inf
	 * ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
	 *
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public Set<Tuple> zrevrangeByScoreWithScores(Object key, double min, double max, int offset, int count)
			throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<Tuple> memberAndScore;
		try {
			memberAndScore = jedis.zrevrangeByScoreWithScores(generateKey(key).getBytes(), max, min, offset, count);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return memberAndScore;
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
	 *
	 * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
	 *
	 * @param key
	 * @param member
	 * @return 如果 member 是有序集 key 的成员，返回 member 的排名。
	 *
	 *         如果 member 不是有序集 key 的成员，返回 nil 。
	 * @throws Exception
	 */
	public Long zrank(Object key, byte[] member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long rank;
		try {
			rank = jedis.zrank(generateKey(key).getBytes(), member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return rank;
	}

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 *
	 * 当 key 存在但不是有序集类型时，返回一个错误。
	 *
	 * @param key
	 * @param member
	 * @return 被成功移除的成员的数量，不包括被忽略的成员。
	 * @throws Exception
	 */
	public Long zrem(Object key, byte[] member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long removeCount;
		try {
			removeCount = jedis.zrem(generateKey(key).getBytes(), member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return removeCount;
	}

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 *
	 * 当 key 存在但不是有序集类型时，返回一个错误。
	 *
	 * @param key
	 * @param member
	 * @return 被成功移除的成员的数量，不包括被忽略的成员。
	 * @throws Exception
	 */
	public Long zrem(Object key, String... member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long removeCount;
		try {
			removeCount = jedis.zrem(generateKey(key), member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return removeCount;
	}

	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。
	 *
	 * 区间分别以下标参数 start 和 stop 指出，包含 start 和 stop 在内。
	 *
	 *
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 *
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return 被移除成员的数量
	 * @throws Exception
	 */
	public Long zremrangeByRank(Object key, long start, long end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long removeCount;
		try {
			removeCount = jedis.zremrangeByRank(generateKey(key).getBytes(), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return removeCount;
	}

	/**
	 * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return 被移除成员的数量
	 * @throws Exception
	 */
	public Long zremrangeByScore(Object key, long min, long max) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long removeCount;
		try {
			removeCount = jedis.zremrangeByScore(generateKey(key).getBytes(), min, max);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return removeCount;
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。
	 *
	 * 排名以 0 为底，也就是说， score 值最大的成员排名为 0
	 *
	 * @param key
	 * @param member
	 * @return 如果 member 是有序集 key 的成员，返回 member 的排名。
	 *
	 *         如果 member 不是有序集 key 的成员，返回 nil 。
	 * @throws Exception
	 */
	public Long zrevrank(Object key, byte[] member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long rank;
		try {
			rank = jedis.zrevrank(generateKey(key).getBytes(), member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return rank;
	}

	/**
	 * 返回有序集 key 中，成员 member 的 score 值。
	 *
	 * 如果 member 元素不是有序集 key 的成员，或 key 不存在，返回 nil
	 *
	 * @param key
	 * @param member
	 * @return member 成员的 score 值
	 * @throws Exception
	 */
	public Double zscore(Object key, byte[] member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Double score;
		try {
			score = jedis.zscore(generateKey(key).getBytes(), member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return score;
	}

	/**
	 * 在所有score相同的情况下根据成员字典排序返回成员字典在指定范围的成员列表。 合法的 min 和 max 参数必须包含 ( 或者 [ ， 其中
	 * ( 表示开区间（指定的值不会被包含在范围之内）， 而 [ 则表示闭区间（指定的值会被包含在范围之内）。
	 *
	 * 特殊值 + 和 - 在 min 参数以及 max 参数中具有特殊的意义， 其中 + 表示正无限， 而 - 表示负无限。 因此，
	 * 向一个所有成员的分值都相同的有序集合发送命令 ZRANGEBYLEX <zset> - + ， 命令将返回有序集合中的所有元素
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return 数组回复：一个列表，列表里面包含了有序集合在指定范围内的成员
	 * @throws Exception
	 */
	public Set<byte[]> zrangeByLex(Object key, byte[] start, byte[] end) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> score;
		try {
			score = jedis.zrangeByLex(generateKey(key).getBytes(), start, end);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return score;
	}

	/**
	 *
	 * 在所有score相同的情况下根据成员字典排序返回成员字典在指定范围内从offset开始的count个成员。 合法的 min 和 max
	 * 参数必须包含 ( 或者 [ ， 其中 ( 表示开区间（指定的值不会被包含在范围之内）， 而 [ 则表示闭区间（指定的值会被包含在范围之内）。
	 *
	 * 特殊值 + 和 - 在 min 参数以及 max 参数中具有特殊的意义， 其中 + 表示正无限， 而 - 表示负无限。 因此，
	 * 向一个所有成员的分值都相同的有序集合发送命令 ZRANGEBYLEX <zset> - + ， 命令将返回有序集合中的所有元素
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @param offset
	 * @param count
	 * @return 数组回复：一个列表，列表里面包含了有序集合在指定范围内的成员
	 * @throws Exception
	 */
	public Set<byte[]> zrangeByLex(Object key, byte[] start, byte[] end, int offset, int count) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> score;
		try {
			score = jedis.zrangeByLex(generateKey(key).getBytes(), start, end, offset, count);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return score;
	}

	/**
	 * 对于一个所有成员的分值都相同的有序集合键 key 来说， 这个命令会返回该集合中， 成员介于 min 和 max 范围内的元素数量
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return 指定范围内的元素数量。
	 * @throws Exception
	 */
	public Long zlexcount(Object key, byte[] min, byte[] max) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long count;
		try {
			count = jedis.zlexcount(generateKey(key).getBytes(), min, max);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return count;
	}

	/**
	 * 对于一个所有成员的分值都相同的有序集合键 key 来说， 这个命令会移除该集合中， 成员介于 min 和 max 范围内的所有元素
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return 被移除的元素数量
	 * @throws Exception
	 */
	public Long zremrangeByLex(Object key, byte[] min, byte[] max) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long count;
		try {
			count = jedis.zremrangeByLex(generateKey(key).getBytes(), min, max);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return count;
	}

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
	 *
	 * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
	 *
	 * 当 key 不是集合类型时，返回一个错误。
	 *
	 * @param key
	 * @param members
	 * @return 添加成功的元素数量
	 * @throws Exception
	 */
	public Long sadd(Object key, byte[] members) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long affectedCount;
		try {
			affectedCount = jedis.sadd(generateKey(key).getBytes(), members);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return affectedCount;
	}

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
	 *
	 * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
	 *
	 * 当 key 不是集合类型时，返回一个错误。
	 *
	 * @param key
	 * @param members
	 * @return 添加成功的元素数量
	 * @throws Exception
	 */
	public Long sadd(Object key, String members) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long affectedCount;
		try {
			affectedCount = jedis.sadd(generateKey(key), members);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return affectedCount;
	}

	/**
	 * 集合中元素的数量
	 *
	 * @param key
	 * @return 集合中元素的数量
	 * @throws Exception
	 */
	public Long scard(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long count;
		try {
			count = jedis.scard(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return count;
	}

	/**
	 * 判断 member 元素是否集合 key 的成员
	 *
	 * @param key
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public Boolean sismember(Object key, String member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Boolean contain;
		try {
			contain = jedis.sismember(generateKey(key), member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return contain;
	}

	/**
	 * 判断 member 元素是否集合 key 的成员
	 *
	 * @param key
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public Boolean sismember(Object key, byte[] member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Boolean contain;
		try {
			contain = jedis.sismember(generateKey(key).getBytes(), member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return contain;
	}

	/**
	 * 返回集合 key 中的所有成员。
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Set<byte[]> smembers(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> members;
		try {
			members = jedis.smembers(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return members;
	}

	/**
	 * 返回集合 key 中的所有字符串成员。
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Set<String> smembersStr(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<String> members;
		try {
			members = jedis.smembers(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return members;
	}

	/**
	 * 移除并返回集合中的一个随机元素。
	 *
	 * 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String spopStr(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String member;
		try {
			member = jedis.spop(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return member;
	}

	/**
	 * 移除并返回集合中的一个随机元素。
	 *
	 * 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public byte[] spop(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		byte[] member;
		try {
			member = jedis.spop(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return member;
	}

	/**
	 * 返回随机元素，不移除元素
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public byte[] srandmember(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		byte[] member;
		try {
			member = jedis.srandmember(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return member;
	}

	/**
	 * 返回随机元素，不移除元素
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String srandmemberStr(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String member;
		try {
			member = jedis.srandmember(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return member;
	}

	/**
	 * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count
	 * 大于等于集合基数，那么返回整个集合。 ?如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count
	 * 的绝对值。
	 *
	 * 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 SRANDMEMBER 则仅仅返回随机元素，而不对集合进行任何改动
	 *
	 *
	 * @param key
	 * @param count
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List srandmember(Object key, int count) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		List member;
		try {
			member = jedis.srandmember(generateKey(key).getBytes(), count);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return member;
	}

	/**
	 * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count
	 * 大于等于集合基数，那么返回整个集合。 ?如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count
	 * 的绝对值。
	 *
	 * 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 SRANDMEMBER 则仅仅返回随机元素，而不对集合进行任何改动
	 *
	 * @param key
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public List<String> srandmemberStr(Object key, int count) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		List<String> member;
		try {
			member = jedis.srandmember(generateKey(key), count);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return member;
	}

	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略
	 *
	 * @param key
	 * @param member
	 * @return 被成功移除的元素的数量，不包括被忽略的元素
	 * @throws Exception
	 */
	public Long srem(Object key, String member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long count;
		try {
			count = jedis.srem(generateKey(key), member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return count;
	}

	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略
	 *
	 * @param key
	 * @param member
	 * @return 被成功移除的元素的数量，不包括被忽略的元素
	 * @throws Exception
	 */
	public Long srem(Object key, byte[][] member) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long count;
		try {
			count = jedis.srem(generateKey(key).getBytes(), member);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return count;
	}

	/**
	 * 从游标cursor开始迭代主键为key的集合内的元素，返回不 定数量元素和下次使用的游标，迭代完了返回游标0
	 *
	 * @param key
	 * @param cursor
	 * @return
	 * @throws Exception
	 */
	public ScanResult<String> sscan(Object key, String cursor) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		ScanResult<String> result;
		try {
			result = jedis.sscan(generateKey(key), cursor);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略
	 *
	 * @param key
	 * @param fields
	 * @return 被成功移除的域的数量，不包括被忽略的域。
	 * @throws Exception
	 */
	public Long hdel(Object key, String... fields) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long count;
		try {
			count = jedis.hdel(generateKey(key), fields);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return count;
	}

	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略
	 *
	 * @param key
	 * @param fields
	 * @return 被成功移除的域的数量，不包括被忽略的域。
	 * @throws Exception
	 */
	public Long hdel(Object key, byte[]... fields) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long count;
		try {
			count = jedis.hdel(generateKey(key).getBytes(), fields);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return count;
	}

	/**
	 * 查看哈希表 key 中，给定域 field 是否存在
	 *
	 * @param key
	 * @param field
	 * @return 如果哈希表含有给定域，返回 1 。 如果哈希表不含有给定域，或 key 不存在，返回 0
	 * @throws Exception
	 */
	public Boolean hexists(Object key, byte[] field) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Boolean exists;
		try {
			exists = jedis.hexists(generateKey(key).getBytes(), field);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return exists;
	}

	/**
	 * 查看哈希表 key 中，给定域 field 是否存在
	 *
	 * @param key
	 * @param field
	 * @return 如果哈希表含有给定域，返回 1 。 如果哈希表不含有给定域，或 key 不存在，返回 0
	 * @throws Exception
	 */
	public Boolean hexists(Object key, String field) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Boolean exists;
		try {
			exists = jedis.hexists(generateKey(key), field);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return exists;
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 *
	 * @param key
	 * @param field
	 * @return 给定域的值。 当给定域不存在或是给定 key 不存在时，返回 nil
	 * @throws Exception
	 */
	public String hget(Object key, String field) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String value;
		try {
			value = jedis.hget(generateKey(key), field);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 *
	 * @param key
	 * @param field
	 * @return 给定域的值。 当给定域不存在或是给定 key 不存在时，返回 nil
	 * @throws Exception
	 */
	public byte[] hgetBytes(Object key, byte[] field) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		byte[] value;
		try {
			value = jedis.hget(generateKey(key).getBytes(), field);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。 在返回值里，紧跟每个域名(field
	 * name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 *
	 * @param key
	 * @return 以列表形式返回哈希表的域和域的值。 若 key 不存在，返回空列表。
	 *
	 * @throws Exception
	 */
	public Map<byte[], byte[]> hgetAllBytes(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Map<byte[], byte[]> value;
		try {
			value = jedis.hgetAll(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。 在返回值里，紧跟每个域名(field
	 * name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 *
	 * @param key
	 * @return 以列表形式返回哈希表的域和域的值。 若 key 不存在，返回空列表。
	 *
	 * @throws Exception
	 */
	public Map<String, String> hgetAll(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Map<String, String> value;
		try {
			value = jedis.hgetAll(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment 。
	 *
	 * 增量也可以为负数，相当于对给定域进行减法操作。
	 *
	 * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
	 *
	 * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 *
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 *
	 * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
	 *
	 * @param key
	 * @param field
	 * @param amount
	 * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值
	 * @throws Exception
	 */
	public long hincrBy(Object key, String field, long amount) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		long newValue;
		try {
			newValue = jedis.hincrBy(generateKey(key), field, amount);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return newValue;
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment 。
	 *
	 * 增量也可以为负数，相当于对给定域进行减法操作。
	 *
	 * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
	 *
	 * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 *
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 *
	 * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
	 *
	 * @param key
	 * @param field
	 * @param amount
	 * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值
	 * @throws Exception
	 */
	public long hincrBy(Object key, byte[] field, long amount) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		long newValue;
		try {
			newValue = jedis.hincrBy(generateKey(key).getBytes(), field, amount);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return newValue;
	}

	/**
	 * 为哈希表 key 中的域 field 加上浮点数增量 increment 。
	 *
	 * 如果哈希表中没有域 field ，那么 HINCRBYFLOAT 会先将域 field 的值设为 0 ，然后再执行加法操作。
	 *
	 * 如果键 key 不存在，那么 HINCRBYFLOAT 会先创建一个哈希表，再创建域 field ，最后再执行加法操作。
	 *
	 * 当以下任意一个条件发生时，返回一个错误：
	 *
	 * 域 field 的值不是字符串类型(因为 redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型） 域 field
	 * 当前的值或给定的增量 increment 不能解释(parse)为双精度浮点数(double precision floating point
	 * number)
	 *
	 * @param key
	 * @param field
	 * @param amount
	 * @return 执行加法操作之后 field 域的值
	 * @throws Exception
	 */
	public Double hincrByFloat(Object key, byte[] field, double amount) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Double newValue;
		try {
			newValue = jedis.hincrByFloat(generateKey(key).getBytes(), field, amount);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return newValue;
	}

	/**
	 * 为哈希表 key 中的域 field 加上浮点数增量 increment 。
	 *
	 * 如果哈希表中没有域 field ，那么 HINCRBYFLOAT 会先将域 field 的值设为 0 ，然后再执行加法操作。
	 *
	 * 如果键 key 不存在，那么 HINCRBYFLOAT 会先创建一个哈希表，再创建域 field ，最后再执行加法操作。
	 *
	 * 当以下任意一个条件发生时，返回一个错误：
	 *
	 * 域 field 的值不是字符串类型(因为 redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型） 域 field
	 * 当前的值或给定的增量 increment 不能解释(parse)为双精度浮点数(double precision floating point
	 * number)
	 *
	 * @param key
	 * @param field
	 * @param amount
	 * @return 执行加法操作之后 field 域的值
	 * @throws Exception
	 */
	public Double hincrByFloat(Object key, String field, double amount) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Double newValue;
		try {
			newValue = jedis.hincrByFloat(generateKey(key), field, amount);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return newValue;
	}

	/**
	 * 返回哈希表 key 中的所有域。
	 *
	 * @param key
	 * @return 一个包含哈希表中所有域的表。 当 key 不存在时，返回一个空表。
	 *
	 * @throws Exception
	 */
	public Set<String> hkeys(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<String> keys;
		try {
			keys = jedis.hkeys(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return keys;
	}

	/**
	 * 返回哈希表 key 中的所有域。
	 *
	 * @param key
	 * @return 一个包含哈希表中所有域的表。 当 key 不存在时，返回一个空表。
	 *
	 * @throws Exception
	 */
	public Set<byte[]> hkeysBytes(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Set<byte[]> keys;
		try {
			keys = jedis.hkeys(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return keys;
	}

	/**
	 * 返回哈希表中域的数量。 当 key 不存在时，返回 0 。
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Long hlen(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long size;
		try {
			size = jedis.hlen(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return size;
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。
	 *
	 * 如果给定的域不存在于哈希表，那么返回一个 nil 值。
	 *
	 * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。
	 *
	 * @param key
	 * @param fields
	 * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样
	 * @throws Exception
	 */
	public List<byte[]> hmget(Object key, byte[]... fields) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		List<byte[]> values;
		try {
			values = jedis.hmget(generateKey(key).getBytes(), fields);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return values;
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。
	 *
	 * 如果给定的域不存在于哈希表，那么返回一个 nil 值。
	 *
	 * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。
	 *
	 * @param key
	 * @param fields
	 * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样
	 * @throws Exception
	 */
	public List<String> hmget(Object key, String[] fields) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		List<String> values;
		try {
			values = jedis.hmget(generateKey(key), fields);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return values;
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
	 *
	 * 此命令会覆盖哈希表中已存在的域。
	 *
	 * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
	 *
	 * @param key
	 * @param fields
	 * @return 如果命令执行成功，返回 OK 。 当 key 不是哈希表(hash)类型时，返回一个错误
	 * @throws Exception
	 */
	public String hmset(Object key, Map<String, String> fields) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String status;
		try {
			status = jedis.hmset(generateKey(key), fields);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
	 *
	 * 此命令会覆盖哈希表中已存在的域。
	 *
	 * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
	 *
	 * @param key
	 * @param fields
	 * @return 如果命令执行成功，返回 OK 。 当 key 不是哈希表(hash)类型时，返回一个错误
	 * @throws Exception
	 */
	public String hmsetBytes(Object key, Map<byte[], byte[]> fields) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		String status;
		try {
			status = jedis.hmset(generateKey(key).getBytes(), fields);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。
	 *
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
	 *
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。 如果哈希表中域 field
	 *         已经存在且旧值已被新值覆盖，返回 0 。
	 *
	 * @throws Exception
	 */
	public Long hset(Object key, byte[] field, byte[] value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long status;
		try {
			status = jedis.hset(generateKey(key).getBytes(), field, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。
	 *
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
	 *
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
	 *
	 * @param key
	 * @param field
	 * @param field
	 * @param value
	 * @param value
	 * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。 如果哈希表中域 field
	 *         已经存在且旧值已被新值覆盖，返回 0 。
	 *
	 * @throws Exception
	 */
	public Long hset(Object key, String field, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long status;
		try {
			status = jedis.hset(generateKey(key), field, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
	 *
	 * 若域 field 已经存在，该操作无效。
	 *
	 * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @return 设置成功，返回 1 。 如果给定域已经存在且没有操作被执行，返回 0
	 * @throws Exception
	 */
	public Long hsetnx(Object key, String field, String value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long status;
		try {
			status = jedis.hsetnx(generateKey(key), field, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
	 *
	 * 若域 field 已经存在，该操作无效。
	 *
	 * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
	 *
	 * @param key
	 * @param field
	 * @param field
	 * @param value
	 * @param value
	 * @return 设置成功，返回 1 。 如果给定域已经存在且没有操作被执行，返回 0
	 * @throws Exception
	 */
	public Long hsetnx(Object key, byte[] field, byte[] value) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Long status;
		try {
			status = jedis.hsetnx(generateKey(key).getBytes(), field, value);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return status;
	}

	/**
	 * 返回哈希表 key 中所有域的值。
	 *
	 * @param key
	 * @return 一个包含哈希表中所有值的表。 当 key 不存在时，返回一个空表。
	 *
	 * @throws Exception
	 */
	public Collection<byte[]> hvalsBytes(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Collection<byte[]> values;
		try {
			values = jedis.hvals(generateKey(key).getBytes());
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return values;
	}

	/**
	 * 返回哈希表 key 中所有域的值。
	 *
	 * @param key
	 * @return 一个包含哈希表中所有值的表。 当 key 不存在时，返回一个空表。
	 *
	 * @throws Exception
	 */
	public Collection<String> hvals(Object key) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		Collection<String> values;
		try {
			values = jedis.hvals(generateKey(key));
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return values;
	}

	/**
	 * 返回的每个元素都是一个键值对，一个键值对由一个键和一个值组成。
	 *
	 * @param key
	 * @param cursor
	 * @return
	 * @throws Exception
	 */
	public ScanResult<Entry<String, String>> hscan(Object key, String cursor) throws Exception {
		Jedis jedis = getJedisPool().getResource();
		ScanResult<Entry<String, String>> values;
		try {
			values = jedis.hscan(generateKey(key), cursor);
		} catch (Exception e) {
			if (jedis != null) {
				jedis.disconnect();
			}
			throw e;
		} finally {
			jedis.close();
		}
		return values;
	}

	private String generateKey(Object key) {
		return nameSpace + key;
	}

	
}
