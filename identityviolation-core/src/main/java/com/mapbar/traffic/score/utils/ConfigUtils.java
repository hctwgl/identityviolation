package com.mapbar.traffic.score.utils;

import java.util.HashMap;
import java.util.Map;

import com.mapbar.traffic.score.db.DbHelp;

public class ConfigUtils {

	private static Map<String, String> citySource = new HashMap<String, String>();

	private static Map<String, String> citySourceRule = new HashMap<String, String>();

	static {
		init();
	}

	public static void init() {
		citySource = DbHelp.getCitySource();
		citySourceRule = DbHelp.getCitySourceRole();
	}

	public synchronized static void refresh() {
		citySource.clear();
		citySourceRule.clear();
		init();
	}

	public static int updateConfig(String cityPy, String className, String usable) {
		int n = DbHelp.updateConfig(cityPy, className, usable);
		return n;
	}

	public static int updateCity(String cityPys, String usable) {
		int n = DbHelp.updateCitys(cityPys, usable);
		return n;
	}

	public static String getCitySources(String key) {
		return citySource.get(key);
	}

	public static String getCitySourceRole(String citypy, String source) {
		return citySourceRule.get(citypy + "," + source);
	}
}
