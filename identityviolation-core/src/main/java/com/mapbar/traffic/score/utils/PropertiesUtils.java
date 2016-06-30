package com.mapbar.traffic.score.utils;

import java.util.ResourceBundle;

public class PropertiesUtils {

	private static ResourceBundle bundle;

	// 初始化资源文件
	static {
		bundle = ResourceBundle.getBundle("violation");
	}

	private PropertiesUtils() {
	}

	public static String getProValue(String key) {
		return bundle.getString(key);
	}

}
