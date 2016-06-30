package com.mapbar.traffic.score.redis;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 非切片连接池
 */
public class RedisDBUtil extends RedisSource {

	private static JedisSentinelPool pool;
	static {
		init();
	}

	private static void init() {

		String[] addressArr = redis_address.split(":");
		Set<String> set = new HashSet<String>();
		set.add(addressArr[0] + ":" + addressArr[1]);
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxIdle(Integer.valueOf(max_idle));
		config.setMaxTotal(Integer.valueOf(max_active));
		config.setMaxWaitMillis(Long.valueOf(max_wait));
		config.setTestOnBorrow(Boolean.valueOf(isTest));
		 pool = new JedisSentinelPool(addressArr[2], set, config);
		//pool = new JedisSentinelPool(addressArr[2], set, config, passwd);

	}

	public static Jedis getRedisTemplate() {
		Jedis Jedis = pool.getResource();
		return Jedis;
	}

	public static void setValue(byte[] key, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			jedis.set(key, value);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
	}

	public static void setValue(String key, String value) {
		Jedis jedis = null;
		try {

			jedis = RedisDBUtil.getRedisTemplate();
			jedis.set(key, value);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
	}

	public static void setExpireValue(byte[] key, byte[] value, int second) {
		Jedis jedis = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			jedis.set(key, value);
			jedis.expire(key, second);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
	}

	public static void setExpireValue(String key, String value, int second) {
		Jedis jedis = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			jedis.set(key, value);
			jedis.expire(key, second);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
	}

	public static void setHashValue(String mapName, String key, String value) {
		Jedis jedis = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			jedis.hset(mapName, key, value);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
	}

	public static void setHashValue(byte[] mapName, byte[] key, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			jedis.hset(mapName, key, value);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
	}

	public static void zadd(String key, Map<String, Double> scoreMembers) {
		Jedis jedis = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			jedis.zadd(key, scoreMembers);
			// jedis.z
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
	}

	public static Set<String> zrevrank(String key, int num) {
		Jedis jedis = null;
		Set<String> result = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			result = jedis.zrevrange(key, 0, num);
			RedisDBUtil.closeJedis(jedis);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return result;
	}

	public static long zadd(String key, String value, double score) {
		Jedis jedis = null;
		long result = 0;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			result = jedis.zadd(key, score, value);
			RedisDBUtil.closeJedis(jedis);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return result;
	}

	public static byte[] getValue(byte[] key) {
		Jedis jedis = null;
		byte[] re = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			re = jedis.get(key);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public static String getValue(String key) {
		Jedis jedis = null;
		String re = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			re = jedis.get(key);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public static String getHashValue(String mapName, String key) {
		Jedis jedis = null;
		String re = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			re = jedis.hget(mapName, key);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public static byte[] getHashValue(byte[] mapName, byte[] key) {
		Jedis jedis = null;
		byte[] re = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			re = jedis.hget(mapName, key);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public static Map<String, String> getAllKeys(String mapName) {
		Jedis jedis = null;
		Map<String, String> re = null;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			re = jedis.hgetAll(mapName);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public static long incr(String key) {
		Jedis jedis = null;
		long re = 0;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			re = jedis.incr(key);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public static long setHashHincr(String key, String field, long value) {
		Jedis jedis = null;
		long re = 0;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			re = jedis.hincrBy(key, field, value);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public static long del(String key) {
		Jedis jedis = null;
		long re = 0;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			re = jedis.del(key);
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public static long zrem(String key, String... members) {
		Jedis jedis = null;
		long re = 0;
		try {
			jedis = RedisDBUtil.getRedisTemplate();
			re = jedis.zrem(key, members);
			// jedis.z
			RedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			RedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	/**
	 * 正常连接池回收
	 * 
	 * @param jedis
	 */
	public static void closeJedis(Jedis jedis) {
		if (jedis != null) {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 异常连接池回收
	 * 
	 * @param jedis
	 */
	public static void closeBreakJedis(Jedis jedis) {
		if (jedis != null) {
			pool.returnBrokenResource(jedis);
		}
	}
}
