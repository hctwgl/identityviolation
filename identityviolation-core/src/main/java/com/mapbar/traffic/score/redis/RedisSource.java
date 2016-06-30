package com.mapbar.traffic.score.redis;

import com.mapbar.traffic.score.utils.PropertiesUtils;

/**
 * 
 * redis的常量配置
 */
public class RedisSource {
	// redis的服务器地址，满足分片
	protected static final String redis_address = PropertiesUtils.getProValue("redis.host");
	// 设置最大的连接数
	protected static final String max_active = PropertiesUtils.getProValue("redis.maxActive");
	// 设置池中最大的空闲数量
	protected static final String max_idle = PropertiesUtils.getProValue("redis.maxIdle");
	// 连接等待的最大时间
	protected static final String max_wait = PropertiesUtils.getProValue("redis.maxWait");
	// 连接超时时间
	protected static final String timeout = PropertiesUtils.getProValue("redis.timeout");
	// 是否进行测试连接
	protected static final String isTest = PropertiesUtils.getProValue("redis.isTest");
	// redis 连接密码
	protected static final String passwd =PropertiesUtils.getProValue("redis.passwd");
}
