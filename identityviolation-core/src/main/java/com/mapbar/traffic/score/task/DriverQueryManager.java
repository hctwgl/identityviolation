package com.mapbar.traffic.score.task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 管理积分查询时间 积分查询是否完成
 * 
 * 
 */
public class DriverQueryManager {

	private DriverQueryManager() {
	}

	private static DriverQueryManager instance = new DriverQueryManager();

	private static Set<String> taskKeyset = new HashSet<String>();

	private static Map<String, Long> lastQueryTimeMap = new HashMap<String, Long>();

	public static DriverQueryManager getInstance() {
		return instance;
	}

	public Long getTime(String key) {
		return lastQueryTimeMap.get(key) == null ? null : lastQueryTimeMap.get(key);
	}

	public void putTime(String key, Long time) {
		lastQueryTimeMap.put(key, time);
	}

	public boolean isTaskOk(String key) {
		return !taskKeyset.contains(key);
	}

	public void addTaskKey(String key) {
		taskKeyset.add(key);
	}

	public boolean hasTaskKey(String key) {
		return taskKeyset.contains(key);
	}

	public void removeTaskKey(String key) {
		synchronized (this) {
			taskKeyset.remove(key);
		}
	}
}
