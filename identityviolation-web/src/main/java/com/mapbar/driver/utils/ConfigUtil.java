package com.mapbar.driver.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.math.NumberUtils;

public class ConfigUtil {

	public static ResourceBundle bundle = ResourceBundle.getBundle("config", Locale.getDefault());
	public static final String WXConfig = bundle.getString("WXConfig");
	public static final int Sleep = NumberUtils.toInt(bundle.getString("Sleep"));
	public static final String Ipchick = bundle.getString("Ipchick");
	/** 图吧导航客户端 用于用户中心token检测的url */
	public static final String CheckToken = bundle.getString("CheckToken");
	/** OBD客户端 用于用户中心token检测的url 与图吧导航的域名不同 */
	// public static final String OBDCheckToken = bundle.getString("OBDCheckToken");
	// 下边是邮件配合 用于商务接口
	public static final String MailName = bundle.getString("MailName");
	public static final String MailPassword = bundle.getString("MailPassword");
	public static final String Ccs = bundle.getString("Ccs");

	/**
	 * 通过键获取值
	 * 
	 * @param key
	 * @return
	 */
	public static final String get(String key) {
		return bundle.getString(key);
	}

}
