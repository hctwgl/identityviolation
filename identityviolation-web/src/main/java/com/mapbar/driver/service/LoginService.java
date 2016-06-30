package com.mapbar.driver.service;

import com.alibaba.fastjson.JSONException;
import com.mapbar.driver.bean.Driver;

public interface LoginService {

	/**
	 * 用户登录接口
	 * 
	 * @param driver
	 * @return
	 * @throws Exception
	 * @throws JSONException
	 */
	public String getLogin(Driver driver) throws Exception;

	/**
	 * 同步锁 进行redis同步 synchronized
	 * 
	 * @param key
	 * @param userID
	 */
	public void saveUserLogin(String key, String userID) throws Exception;
}
