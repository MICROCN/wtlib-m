package com.wtlib.base.memcached;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.CommandFactory;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.MemcachedSessionLocator;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wtlib.base.constants.GlobalConstants;

public class MemCacheClient {
	protected final Logger logger = LoggerFactory.getLogger(MemCacheClient.class);
	private MemcachedClient memcachedClient;
	private String nameSpace;
	private String cacheServerId;
	private int expiry;
	private static Map<String, MemcachedClient> CLIENTS = new HashMap<String, MemcachedClient>();

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		List<String> keys = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			try {
				String key = "k" + i;
				keys.add(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(System.currentTimeMillis() - s);
	}


	private MemcachedClient buildMemcachedClient() {
		MemcachedClient client = null;
		try {
			String userName = GlobalConstants.getString(cacheServerId + ".memcache.userName");
			String password = GlobalConstants.getString(cacheServerId + ".memcache.password");
			String servers = GlobalConstants.getString(cacheServerId + ".memcache.servers");
			String weightStr = GlobalConstants.getString(cacheServerId + ".memcache.weights");
			String commandFactoryClass = GlobalConstants.getString(cacheServerId + ".memcache.command.factory");
			boolean failureMode = GlobalConstants.getBoolean(cacheServerId + ".memcache.failureMode");
			int sessionIdleTimeout = GlobalConstants.getInt(cacheServerId + ".memcache.sessionIdleTimeout");
			int poolSize = GlobalConstants.getInt(cacheServerId + ".memcache.poolSize");
			String sessionLocator = GlobalConstants.getString(cacheServerId + ".memcache.session.locator");
			int opTimeout = GlobalConstants.getInt(cacheServerId + ".memcache.opTimeout");
			boolean enableHeartBeat = GlobalConstants.getBoolean(cacheServerId + ".memcache.enableHeartBeat");
			int compressionThreshold = GlobalConstants.getInt(cacheServerId + ".memcache.compressionThreshold");
			boolean packZeros = GlobalConstants.getBoolean(cacheServerId + ".memcache.packZeros");

			String[] serversArray = servers.split(" ");
			String[] weightsArray = null;
			if (weightStr != null) {
				weightStr.split(",");
			}
			int serversLength = serversArray.length;

			int[] weights = new int[serversLength];
			for (int i = 0; i < serversLength; i++) {
				weights[i] = weightsArray == null ? 1 : Integer.valueOf(weightsArray[i]);
			}
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(servers), weights);

			builder.setConnectionPoolSize(poolSize);

			builder.setFailureMode(failureMode);

			builder.getConfiguration().setSessionIdleTimeout(sessionIdleTimeout);
			// 用户和密码验证
			if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
				for (String server : serversArray) {
					builder.addAuthInfo(AddrUtil.getOneAddress(server), AuthInfo.typical(userName, password));
				}
			}

			// TextCommandFactory(默认),KestrelCommandFactory、BinaryCommandFactory
			CommandFactory commandFactory = (CommandFactory) Class.forName(commandFactoryClass).newInstance();
			builder.setCommandFactory(commandFactory);

			MemcachedSessionLocator memcachedSessionLocator = (MemcachedSessionLocator) Class.forName(sessionLocator)
					.newInstance();
			builder.setSessionLocator(memcachedSessionLocator);

			client = builder.build();

			client.setOpTimeout(opTimeout);

			client.setEnableHeartBeat(enableHeartBeat);

			@SuppressWarnings("rawtypes")
			Transcoder transcoder = client.getTranscoder();

			transcoder.setCompressionThreshold(compressionThreshold);

			transcoder.setPackZeros(packZeros);

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return client;
	}

	public MemCacheClient(MemcacheAdapter memcacheAdapter) {
		try {
			this.nameSpace = memcacheAdapter.getNameSpace();
			this.expiry = memcacheAdapter.getExpiry();
			this.cacheServerId = memcacheAdapter.getCacheServerId();
			memcachedClient = CLIENTS.get(cacheServerId);
			if (memcachedClient == null) {
				memcachedClient = buildMemcachedClient();
				CLIENTS.put(cacheServerId, memcachedClient);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 获取域内的key前缀
	 *
	 * @return
	 */
	public String getKeyPrefix() {
		return nameSpace;
	}

	public String getCacheServerId() {
		return cacheServerId;
	}

	/**
	 * 根据key删除对象，返回删除是否成功
	 *
	 * @param key
	 * @return
	 */
	public boolean removeWithReply(Object key) {
		try {
			return memcachedClient.delete(generateKey(key));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}

	/**
	 * 根据key删除对象，不等待返回响应
	 *
	 * @param key
	 */
	public void remove(Object key) {
		try {
			memcachedClient.deleteWithNoReply(generateKey(key));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 根据key获取存储对象
	 *
	 * @param key
	 * @return
	 */
	public <T> T get(Object key) {
		try {
			return memcachedClient.get(generateKey(key));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 根据多个key获取多个key的值，在多台机器下不保证顺序
	 *
	 * @param keyCollections
	 * @return
	 */
	public <T, E> Map<String, T> getMuti(Collection<E> keyCollections) {
		try {
			return memcachedClient.get(mutiKey(keyCollections));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 *
	 * @param keyCollections
	 * @return
	 */
	private <E> List<String> mutiKey(Collection<E> keyCollections) {
		List<String> keys = new ArrayList<String>();
		for (Object key : keyCollections) {
			keys.add(generateKey(key));
		}
		return keys;
	}

	/**
	 * 根据key获取存储对象的同时更新这个key新的超时时间为expiry秒后失效
	 *
	 * @param key
	 * @return
	 */
	public <T> T getAndTouch(Object key) {
		try {
			return memcachedClient.getAndTouch(generateKey(key), expiry);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 根据key获取存储对象的同时更新这个key新的超时时间为exp秒后失效
	 *
	 * @param key
	 * @return
	 */
	public <T> T getAndTouch(Object key, int exp) {
		try {
			return memcachedClient.getAndTouch(generateKey(key), exp);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 存储对象，有效期为默认expiry秒，如果这个key原来有值则覆盖原来的值
	 *
	 * @param key
	 * @param object
	 * @return
	 */
	public <T> boolean setWithReply(Object key, T object) {
		return setWithReply(key, expiry, object);
	}

	/**
	 * 存储对象，有效期为exp秒（单位秒），如果这个key原来有值则覆盖原来的值
	 *
	 * @param key
	 * @param exp
	 * @param object
	 * @return
	 */
	public <T> boolean setWithReply(Object key, int exp, T object) {
		try {
			if (object == null) {
				return false;
			}
			return memcachedClient.set(generateKey(key), exp, object);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}

	/**
	 * 异步存储对象，不等待返回响应，有效期为默认的30天，如果这个key原来有值则覆盖原来的值，因为是异步，
	 * 所以如果调用这个方法后马上调用get方法可能返回null
	 *
	 * @param key
	 * @param object
	 */
	public <T> void set(Object key, T object) {
		set(key, expiry, object);
	}

	/**
	 * 异步存储对象，不等待返回响应，有效期为exp秒（单位秒），如果这个key原来有值则覆盖原来的值
	 *
	 * @param key
	 * @param exp
	 * @param object
	 */
	public <T> void set(Object key, int exp, T object) {
		try {
			if (object == null) {
				return;
			}
			memcachedClient.setWithNoReply(generateKey(key), exp, object);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 存储对象，有效期为默认expiry秒，如果这个key原来有值则不存储
	 *
	 * @param key
	 * @param object
	 * @return
	 */
	public <T> boolean addWithReply(Object key, T object) {
		return addWithReply(key, expiry, object);
	}

	/**
	 * 存储对象，有效期为exp秒（单位秒），如果这个key原来有值则不存储
	 *
	 * @param key
	 * @param exp
	 * @param object
	 * @return
	 */
	public <T> boolean addWithReply(Object key, int exp, T object) {
		try {
			if (object == null) {
				return false;
			}
			return memcachedClient.add(generateKey(key), exp, object);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}

	/**
	 * 异步存储对象，不等待返回响应，有效期为默认的30天，如果这个key原来有值则不存储
	 *
	 * @param key
	 * @param object
	 */
	public <T> void add(Object key, T object) {
		add(key, expiry, object);
	}

	/**
	 * 异步存储对象，不等待返回响应，有效期为exp秒（单位秒），如果这个key原来有值则不存储
	 *
	 * @param key
	 * @param exp
	 * @param object
	 */
	public <T> void add(Object key, int exp, T object) {
		try {
			if (object == null) {
				return;
			}
			memcachedClient.addWithNoReply(generateKey(key), exp, object);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 为key对应的值增加amount，如果这个key在缓存中还没值则以0初始化.并且本次修改动作无效,不返回响应
	 * 这个方法不能对set或add进去的值做修改
	 * ，只能支持inc自身初始化,并且第一次初始化的值不能通过get方法返回,初始化之后的修改的值才能通过get获取字符串的形式返回
	 *
	 * @param key
	 * @param amount
	 * @throws Exception
	 */
	public void incrWithNoReply(Object key, long amount) throws Exception {
		if (amount <= 0) {
			throw new Exception("incr值不能小于等于0,需要利用key不存在返回0的机制来判断key是否存在");
		}
		try {
			memcachedClient.incrWithNoReply(generateKey(key), amount);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 为key对应的值增加amount，并返回修改后的值，如果这个key在缓存中还没值则以0初始化并返回0.并且本次修改动作无效,
	 * 如果原来key存在则返回增加后的新值
	 * ,这个方法不能对set或add进去的值做修改，只能支持inc自身初始化,并且第一次初始化的值不能通过get方法返回
	 * ,初始化之后的修改的值才能通过get获取字符串的形式返回
	 *
	 * @param key
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public long incr(Object key, long amount) throws Exception {
		if (amount <= 0) {
			throw new Exception("incr值不能小于等于0,需要利用key不存在返回0的机制来判断key是否存在");
		}
		long newResult = 0;
		try {
			newResult = memcachedClient.incr(generateKey(key), amount);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return newResult;
	}

	/**
	 * 为key对应的值增加amount，并返回修改后的值，如果这个key在缓存中还没值则以initValue初始化并返回initValue。
	 * 这个方法不能对set或add进去的值做修改
	 * ，只能支持inc自身初始化,并且第一次初始化的值不能通过get方法返回,初始化之后的修改的值才能通过get获取字符串的形式返回
	 *
	 * @param key
	 * @param amount
	 * @param initValue
	 * @return
	 * @throws Exception
	 */
	public long incr(Object key, long amount, long initValue) throws Exception {
		long newResult = 0;
		try {
			newResult = memcachedClient.incr(generateKey(key), amount, initValue);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return newResult;
	}

	/**
	 * 为key对应的值减去amount，如果这个key在缓存中还没值则以0初始化.并且本次修改动作无效,不返回响应
	 * 这个方法不能对set或add进去的值做修改
	 * ，只能支持decr自身初始化,并且第一次初始化的值不能通过get方法返回,初始化之后的修改的值才能通过get获取字符串的形式返回
	 *
	 * @param key
	 * @param amount
	 * @throws Exception
	 */
	public void decrWithNoReply(Object key, long amount) throws Exception {
		if (amount <= 0) {
			throw new Exception("decr值不能小于等于0,需要利用key不存在返回0的机制来判断key是否存在");
		}
		try {
			memcachedClient.decrWithNoReply(generateKey(key), amount);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 为key对应的值减去amount，并返回修改后的值，如果这个key在缓存中还没值则以0初始化并返回0，并且本次修改动作无效,
	 * 如果原来key存在则返回增加后的新值
	 * 这个方法不能对set或add进去的值做修改，只能支持inc自身初始化,并且第一次初始化的值不能通过get方法返回
	 * ,初始化之后的修改的值才能通过get获取字符串的形式返回
	 *
	 * @param key
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public long decr(Object key, long amount) throws Exception {
		if (amount <= 0) {
			throw new Exception("decr值不能小于等于0,需要利用key不存在返回0的机制来判断key是否存在");
		}
		long newResult = 0;
		try {
			newResult = memcachedClient.decr(generateKey(key), amount);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return newResult;
	}

	/**
	 * 为key对应的值减去amount，并返回修改后的值，如果这个key在缓存中还没值则以initValue初始化并返回initValue。
	 * 这个方法不能对set或add进去的值做修改
	 * ，只能支持inc自身初始化化,并且第一次初始化的值不能通过get方法返回,初始化之后的修改的值才能通过get获取字符串的形式返回
	 *
	 * @param key
	 * @param amount
	 * @param initValue
	 * @return
	 * @throws Exception
	 */
	public long decr(Object key, long amount, long initValue) throws Exception {
		long newResult = 0;
		try {
			newResult = memcachedClient.decr(generateKey(key), amount, initValue);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return newResult;
	}

	/**
	 * 將value追加到主键为key的值后面,返回是否成功
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean appendWithReply(Object key, Object value) {
		boolean success = false;
		try {
			success = memcachedClient.append(generateKey(key), value);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return success;
	}

	/**
	 * 將value追加到主键为key的值后面,不等待响应
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public void append(Object key, Object value) {
		try {
			memcachedClient.appendWithNoReply(generateKey(key), value);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 將value追加到主键为key的值前面,返回是否成功
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean prependWithReply(Object key, Object value) {
		boolean success = false;
		try {
			success = memcachedClient.prepend(generateKey(key), value);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return success;
	}

	/**
	 * 將value追加到主键为key的值前面,不等待响应
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public void prepend(Object key, Object value) {
		try {
			memcachedClient.prependWithNoReply(generateKey(key), value);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 更新key的新的失效时间为exp秒
	 *
	 * @param key
	 * @param exp
	 * @return
	 */
	public boolean touch(Object key, int exp) {
		boolean touch = false;
		try {
			touch = memcachedClient.touch(generateKey(key), exp);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return touch;
	}

	private String generateKey(Object key) {
		return nameSpace + key;
	}

}