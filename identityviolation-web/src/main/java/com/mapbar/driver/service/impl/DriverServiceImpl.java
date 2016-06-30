package com.mapbar.driver.service.impl;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.driver.bean.Driver;
import com.mapbar.driver.dao.DriverDao;
import com.mapbar.driver.service.DriverService;
import com.mapbar.driver.utils.DateUtil;
import com.mapbar.driver.utils.PostDriver;
import com.mapbar.traffic.score.base.ServiceConfig;

@Service
public class DriverServiceImpl implements DriverService {

	private static final Logger logger = Logger.getLogger(DriverServiceImpl.class);

	@Resource
	private DriverDao driverDao;

	/**
	 * 驾驶证积分查询接口
	 * 
	 * @param driverName
	 * @param driverLicense
	 * @param lissueDate
	 * @param lissueArchive
	 * @param Province
	 * @param cityJC
	 * @param city_pinyin
	 * @param isEffective
	 * @param product
	 * @param effectiveDate
	 * @param ip
	 * @param refresh
	 * @return
	 * @throws IOException
	 */
	public String getWebJiFen(Driver driver) {
		String citypinyin = "";
		citypinyin = driver.getCity_pinyin();
		Date dt = new Date();

		JSONObject retJson = new JSONObject();
		JSONObject queryJson = new JSONObject();

		String retStr = "";
		String city = "";
		String province = "";

		city = driver.getCity();
		province = driver.getProvince();

		// 输入参数
		queryJson.put("ip", driver.getIp());
		queryJson.put("city", city);
		queryJson.put("province", province);
		queryJson.put("citypinyin", citypinyin);
		queryJson.put("product", driver.getProduct());
		String query = queryJson.toJSONString();
		// 不走缓存 直接访问接口
		retStr = PostDriver.getDriverScore(driver);
		// 封装几种返回格式
		if (retStr.indexOf("服务维护中") >= 0) {
			retJson.put("status", "2");
			retJson.put("lists", new JSONArray());
			retJson.put("message", "系统繁忙，请稍后再试");
			retJson.put("time", DateUtil.dateToTimeString(dt));
			retJson.put("province", province);
			retJson.put("city", city);
			retJson.put("product", driver.getProduct());
			logger.info("返回结果" + query + "@@" + retJson.toJSONString() + "@@" + retStr);
			return retJson.toJSONString();
		} else if (retStr.indexOf("查询失败，请稍后再试") >= 0) {
			retJson.put("status", "3");
			retJson.put("lists", new JSONArray());
			retJson.put("message", "查询失败，请稍后再试");
			retJson.put("time", DateUtil.dateToTimeString(dt));
			retJson.put("province", province);
			retJson.put("city", city);
			retJson.put("product", driver.getProduct());
			logger.info("返回结果" + query + "@@" + retJson.toJSONString() + "@@" + retStr);
			return retJson.toJSONString();
		}

		if (StringUtils.isNotBlank(retStr)) {
			if (retStr.indexOf("\"status\":\"ok\"") >= 0) {
				// 产品标识不能为空
				if (StringUtils.isNotEmpty(driver.getProduct())) {
					// 查询成功保存到数据库
					try {
						// 查询驾驶证号唯一
						int count = driverDao.getUserByLicense(driver.getDriver_license());
						if (count == 0) {
							driverDao.instertUser(driver);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			retJson = JSONObject.parseObject(retStr);
		}
		retJson.put("time", DateUtil.dateToTimeString(dt));
		retJson.put("province", province);
		retJson.put("city", city);
		retJson.put("product", driver.getProduct());
		String str = retJson.toJSONString();
		logger.info("返回结果" + query + "@@" + str.toString() + "@@" + retStr);
		return str.toString();
	}

	/**
	 * 专门用于绑定用户信息的接口
	 * 
	 * @throws IOException
	 */
	public String saveUser(Driver driver) {
		// 查询成功保存到数据库
		String citypinyin = "";
		if ("BJ".equals(driver.getCity_pinyin())) {
			citypinyin = "beijing";
		} else {
			citypinyin = driver.getCity_pinyin().toLowerCase();
		}
		String st = "";
		String city = ServiceConfig.htCitylist.get(citypinyin.toLowerCase()).getCityName();
		String province = ServiceConfig.htCitylist.get(citypinyin.toLowerCase()).getProvince();

		if (StringUtils.isEmpty(driver.getDriver_license()) && StringUtils.isEmpty(driver.getProduct()) && StringUtils.isEmpty(city) && StringUtils.isEmpty(province)) {
			// 查询成功保存到数据库
			try {
				// 查询驾驶证号唯一
				int count = driverDao.getUserByLicense(driver.getDriver_license());
				if (count == 0) {
					driverDao.instertUser(driver);
				}
			} catch (Exception e) {
				st = "{\"status\":\"10\",\"message\":\"修改失败\"}";
				e.printStackTrace();
			}
		} else {
			st = "{\"status\":\"1\",\"message\":\"请检查输入参数\"}";
		}
		return st;
	}

	/**
	 * 封装驾驶证查积分接口
	 * 
	 * @param driver
	 * @return
	 * @throws IOException
	 */
	public String getDriverScore(Driver driver) {
		Date dt = new Date();

		JSONObject retJson = new JSONObject();
		JSONObject logJson = new JSONObject();
		String retStr = null;
		retStr = PostDriver.getDriverScore(driver);
		if (retStr.indexOf("很多人在查询哦") >= 0) {
			retJson.put("status", "err");
			retJson.put("msg", "很多人在查询哦，请稍后再试");
			logJson.put("time", DateUtil.dateToTimeString(dt));
			logJson.put("province", driver.getProvince());
			logJson.put("city", driver.getCity());
			logJson.put("citypinyin", driver.getCity_pinyin());
			logJson.put("product", driver.getProduct());
			logger.info("返回结果" + logJson.toJSONString() + "@@" + retJson.toJSONString() + "@@" + retStr);
			return retJson.toJSONString();
		}

		if (StringUtils.isNotBlank(retStr)) {

			if (retStr.indexOf("\"status\":\"ok\"") >= 0) {
				try {
					// 产品标识不能为空
					if (StringUtils.isNotEmpty(driver.getProduct())) {
						// 查询成功保存到数据库
						try {
							// 查询驾驶证号唯一
							int count = driverDao.getUserByLicense(driver.getDriver_license());
							if (count == 0) {
								driverDao.instertUser(driver);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			retJson = JSONObject.parseObject(retStr);
		} else {
			retJson.put("status", "err");
			retJson.put("msg", "服务器繁忙");
		}
		retJson.put("time", DateUtil.dateToTimeString(dt));
		String str = retJson.toJSONString();
		logJson.put("ip", driver.getIp());
		logJson.put("city", driver.getCity());
		logJson.put("product", driver.getProduct());
		logJson.put("citypinyin", driver.getCity_pinyin());
		logger.info("返回结果" + logJson.toJSONString() + "@@" + str.toString() + "@@" + retStr);
		return str.toString();
	}

}
