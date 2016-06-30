package com.mapbar.driver.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.driver.bean.Driver;
import com.mapbar.driver.dao.SynchronousDao;
import com.mapbar.driver.service.LoginService;
import com.mapbar.driver.utils.ConfigUtil;
import com.mapbar.traffic.score.redis.RedisDBUtil;
import com.mapbar.traffic.score.utils.HttpClientUtil;

@Service
public class LoginServiceImpl implements LoginService {
	@Resource
	private SynchronousDao synchronousDao;

	/**
	 * 用户登录接口
	 * 
	 * @param driver
	 * @return
	 * @throws Exception
	 * @throws JSONException
	 */
	public String getLogin(Driver driver) throws Exception {
		String product = driver.getProduct(); // 产品标识
		String key = driver.getKey(); // uuid
		String userID = driver.getUserId(); // 账号标识
		String userToken = driver.getUserToken(); // 有效期
		// 更新关系表
		JSONObject json = new JSONObject();

		if (!"12345".equals(userToken)) {
			int tokenInt = 0;
			if (product.equals("mapbar_trinity")) {
				// 特殊处理客户端提供的产品标识与 用户中心平台的不兼容问题
				tokenInt = HttpClientUtil.checkToken(ConfigUtil.CheckToken + "android_trinity", userToken);
			} else {
				tokenInt = HttpClientUtil.checkToken(ConfigUtil.CheckToken + product, userToken);
			}
			if (tokenInt != 200) {
				json.put("status", "1005");
				json.put("message", "无效的token");
				return json.toString();
			}
		}

		if (StringUtils.isEmpty(key)) {
			json.put("status", "1007");
			json.put("message", "无设备key");
			return json.toString();
		}
		if (StringUtils.isEmpty(userID)) {
			json.put("status", "1008");
			json.put("message", "无用户id");
			return json.toString();
		}
		// 同步redis数据
		saveUserLogin(key, userID);

		List<Driver> list = synchronousDao.getUserLogin(key, userID);
		json.put("status", "1000");
		json.put("message", "成功");
		json.put("driverList", list);

		return json.toString();
	}

	/**
	 * 同步锁 进行redis同步 synchronized
	 * 
	 * @param key
	 * @param userID
	 */
	public synchronized void saveUserLogin(String key, String userID) {
		RedisDBUtil.del("jiashizheng.userLogin." + key);
		RedisDBUtil.del("jiashizheng.userLogin." + userID);

		RedisDBUtil.setValue("jiashizheng.userLogin." + key, userID);
		RedisDBUtil.setValue("jiashizheng.userLogin." + userID, key);
	}
}
