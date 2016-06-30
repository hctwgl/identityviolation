package com.mapbar.driver.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.driver.bean.Driver;
import com.mapbar.driver.dao.SynchronousDao;
import com.mapbar.driver.service.UserSynchronousService;
import com.mapbar.driver.utils.ConfigUtil;
import com.mapbar.driver.utils.DateUtil;
import com.mapbar.driver.utils.StringUtil;
import com.mapbar.traffic.score.redis.RedisDBUtil;
import com.mapbar.traffic.score.utils.HttpClientUtil;

@Service
public class UserSynchronousServiceImpl implements UserSynchronousService {

	private static final Logger logger = Logger.getLogger(UserSynchronousServiceImpl.class);

	@Resource
	private SynchronousDao synchronousDao;

	/**
	 * 用户登录同步接口 功能流程1：删除设备绑定 2：同步账户绑定表
	 * 
	 * @param driver
	 * @return
	 * @throws Exception
	 * @throws JSONException
	 */
	public String getUserSynchronous(Driver driver) throws Exception {
		String result = driver.getJson();
		JSONObject retJson = new JSONObject();
		if (StringUtils.isEmpty(result)) {
			retJson.put("status", "1003");
			retJson.put("message", "请检查json格式");
			return retJson.toString();
		}
		JSONObject jobj = null;
		try {
			jobj = JSONObject.parseObject(result);
		} catch (Exception e) {
			retJson.put("status", "1003");
			retJson.put("message", "请检查json格式");
			return retJson.toString();
		}
		if (StringUtils.isEmpty(driver.getProduct())) {
			retJson.put("status", "1002");
			retJson.put("message", "请检查产品标识");
			return retJson.toString();
		}
		if (StringUtils.isEmpty(jobj.getString("key"))) {
			retJson.put("status", "1007");
			retJson.put("message", "无设备key");
			return retJson.toString();
		}
		if (StringUtils.isEmpty(jobj.getString("userId"))) {
			retJson.put("status", "1008");
			retJson.put("message", "无用户id");
			return retJson.toString();
		}

		// 驾驶证详情参数
		// String baiduId = (String) json1.get("baiduId");
		// String carPushSwitch = (String) json1.get("carPushSwitch");
		String key = jobj.getString("key");
		// String token = (String) json1.get("push_token");
		String product = driver.getProduct();
		String userId = jobj.getString("userId");
		String userToken = jobj.getString("userToken"); // 有效期

		if (!"12345".equals(userToken)) {
			int tokenInt = 0;
			if (product.equals("mapbar_trinity")) {
				// 特殊处理客户端提供的产品标识与 用户中心平台的不兼容问题
				tokenInt = HttpClientUtil.checkToken(ConfigUtil.CheckToken + "android_trinity", userToken);
			} else {
				tokenInt = HttpClientUtil.checkToken(ConfigUtil.CheckToken + product, userToken);
			}
			if (tokenInt != 200) {
				retJson.put("status", "1005");
				retJson.put("message", "无效的token");
				return retJson.toString();
			}
		}

		if (jobj.containsKey("driverList")) {
			List<String> licenseList = new ArrayList<String>();
			JSONArray jsona = jobj.getJSONArray("driverList");
			for (int i = 0; i < jsona.size(); i++) {
				JSONObject json2 = JSONObject.parseObject(jsona.toArray()[i].toString());
				// 查询车辆详情表 验证并更新 车辆信息
				licenseList.add((String) json2.get("hphm"));
			}

			String keyV = RedisDBUtil.getValue("jiashizheng.userLogin." + key);
			String userIDV = RedisDBUtil.getValue("jiashizheng.userLogin." + userId);

			if (!StringUtils.isEmpty(keyV) || !StringUtils.isEmpty(userIDV)) {
				synchronousDao.updateUserSynchronous(driver, licenseList);
			} else {
				retJson.put("status", "1006");
				retJson.put("message", "未登录状态");
				return retJson.toString();
			}

		}
		retJson.put("status", "1000");
		retJson.put("message", "成功");
		return retJson.toString();
	}

	public String getSynchronous(Driver driver) throws Exception {
		String result = driver.getJson();
		if (StringUtils.isEmpty(result)) {
			return "{\"status\":\"1003\",\"message\":\"请检查json格式\"}";
		}
		JSONObject retJson = null;
		try {
			retJson = JSONObject.parseObject(result);
		} catch (Exception e) {
			return "{\"status\":\"1003\",\"message\":\"请检查json格式\"}";
		}
		if (StringUtils.isEmpty(driver.getProduct())) {
			return "{\"status\":\"1002\",\"message\":\"请检查产品标识\"}";
		}
		// 车辆详情参数
		String baiduId = retJson.getString("baiduId");
		// String carPushSwitch = (String) json1.get("carPushSwitch");
		// String imei = (String) json1.get("imei");
		// String model = (String) json1.get("model");
		// String system = (String) json1.get("system");
		String key = retJson.getString("key");
		// String token = (String) json1.get("push_token");
		String vision = retJson.getString("vision");
		String citypinyin = "";
		String uuid = "";
		int intVision = 0;
		try {
			intVision = Integer.parseInt(vision);
		} catch (Exception e) {
			logger.info("版本号转换失败：" + vision);
		}

		// 版本号如何判断
		// 旧版本无百度id 不处理
		if (intVision <= 820000999 && StringUtils.isEmpty(baiduId)) {
			return "{\"status\":\"1004\",\"message\":\"无推送ID\"}";
		}
		if (intVision > 820000999 && StringUtils.isEmpty(key)) {
			UUID newUuid = UUID.randomUUID();
			uuid = newUuid.toString();
		} else if (!StringUtils.isEmpty(key)) {
			uuid = key;
		}

		List<String> licenses = new ArrayList<String>();
		if (retJson.containsKey("driverList")) {
			JSONArray jsona = retJson.getJSONArray("driverList");
			for (int i = 0; i < jsona.size(); i++) {
				JSONObject json2 = JSONObject.parseObject(jsona.toArray()[i].toString());
				// 验证是否更新车辆详情
				if (StringUtils.isEmpty(retJson.get("licenses").toString()) && StringUtils.isEmpty(retJson.getString("change")) && StringUtils.isEmpty(retJson.getString("city"))) {
					// 验证车牌号是否有效 无效不做处理
					if (StringUtil.isCarNumber((String) json2.get("licenses"))) {
						licenses.add((String) json2.get("licenses"));
						if ("0".equals(json2.getString("change")) || "1".equals(json2.getString("change"))) {
							citypinyin = json2.getString("city");
							citypinyin = citypinyin.toLowerCase();
							if ("bj".equals(citypinyin)) {
								citypinyin = "beijing";
							}
						}
					}
				}
			}
		}
		// 更新关系表
		this.synchronousDao.updateSynchronous(driver, licenses);
		logger.info("SynchronousLog" + "{\"status\":\"1000\",\"message\":\"成功\",\"key\":\"" + uuid + "\"}" + "@@" + driver.getProduct() + retJson.toString() + "@@" + DateUtil.getDateTime());
		// 更新账号关系表
		RedisDBUtil.del("jiashizheng.userLogin." + key);
		return "{\"status\":\"1000\",\"message\":\"成功\",\"key\":\"" + uuid + "\"}";
	}

}
