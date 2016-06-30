package com.mapbar.driver.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.driver.bean.City;
import com.mapbar.driver.bean.Driver;
import com.mapbar.driver.service.CityService;
import com.mapbar.driver.service.DriverService;
import com.mapbar.driver.utils.DateUtil;
import com.mapbar.driver.utils.IdcardUtils;
import com.mapbar.driver.utils.StringUtil;

@Controller
@Scope("prototype")
public class DriverController extends BaseController {

	private static final Logger logger = Logger.getLogger(DriverController.class);
	@Resource
	private Driver driver;

	@Resource
	private CityService cityService;

	@Resource
	private DriverService driverService;

	@Override
	protected void getParameter(Form form) {
		logger.info("form query:::" + form);
		for (org.restlet.data.Parameter parameter : form) {
			if ("driver_name".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				try {
					driver.setDriver_name(StringUtil.trimString(parameter.getValue()).toUpperCase());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if ("driver_license".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setDriver_license(StringUtil.trimString(parameter.getValue()));
			} else if ("lissue_date".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setLissue_date(StringUtil.trimString(parameter.getValue()));
			} else if ("lissue_archive".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setLissue_archive(StringUtil.trimString(parameter.getValue()));
			} else if ("is_effective".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setIs_effective(StringUtil.trimString(parameter.getValue()));
			} else if ("city".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setCity(StringUtil.trimString(parameter.getValue().toLowerCase()));
			} else if ("effective_date".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setEffective_date(StringUtil.trimString(parameter.getValue()));
			} else if ("product".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setProduct(StringUtil.trimString(parameter.getValue()));
			} else if ("ip".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setIp(parameter.getValue());
			} else if ("refresh".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setRefresh(parameter.getValue());
			} else if ("sid".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setSid(parameter.getValue());
			} else if ("cs".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setCs(parameter.getValue());
			} else if ("vcode".equalsIgnoreCase(parameter.getName()) && StringUtils.isNotEmpty(parameter.getValue())) {
				driver.setVcode(parameter.getValue());
			}
		}
	}

	@Override
	protected Representation getXMLRepresentation() {
		return null;
	}

	@Override
	protected Representation getJSONRepresentation() {
		String st = "";
		JSONObject retJson = new JSONObject();
		Representation representation = null;
		try {
			Date dt = new Date();
			if (StringUtils.isNotEmpty(driver.getCity())) {
				String city = driver.getCity();
				if ("bj".equals(city)) {
					city = "beijing";
					driver.setCity_pinyin("beijing");
				}
				String isName = "";
				String isLicense = "";
				String isLicensedate = "";
				String isArchive = "";
				String isEffective = "";
				String effectiveDate = "";
				String status = "";
				String errMessage = "";
				// 对旧城市兼容
				if (CityService.oldCLMap.isEmpty()) {
					cityService.getOldCityListMap();
				}
				if (CityService.oldCLMap.containsKey(city.toUpperCase())) {
					city = CityService.oldCLMap.get(city.toUpperCase());
					driver.setCity_pinyin(city);
				}
				if (CityService.cityMap.isEmpty()) {
					cityService.getCityMap();
				}
				// 读取对照表 获取不同城市的 车架号与发动机号 用于后边验证
				if (CityService.cityMap.containsKey(city)) {
					City cm = CityService.cityMap.get(city);
					isName = cm.getIs_name();
					isLicense = cm.getIs_license();
					isArchive = cm.getIs_archive();
					isLicensedate = cm.getIs_licensedate();
					isEffective = cm.getIs_effective();
					effectiveDate = cm.getEffective_date();
					status = cm.getCity_status();
					driver.setPro(cm.getPro());
					driver.setProvince(cm.getProvince());
					driver.setCity(cm.getCity_name());
					driver.setCity_pinyin(city);
					driver.setIsyzm(cm.getIsyzm());
				} else {
					retJson.put("status", "6");
					retJson.put("message", "城市参数非法");
					retJson.put("time", DateUtil.dateToTimeString(dt));
					retJson.put("province", driver.getCity());
					retJson.put("city", driver.getCity());
					retJson.put("product", driver.getProduct());
					// retJson.put("channel", "phone");
					errMessage = retJson.toJSONString();
					logger.info("返回结果" + errMessage);
					representation = new StringRepresentation(errMessage);
					return representation;
				}
				if ("1".equals(driver.getIsyzm())) {
					retJson.put("status", "6");
					retJson.put("message", "该城市查询需要支持验证码，如未弹出验证码图片，请更新系统到最新版本，给您带来的不变请您谅解，谢谢!");
					retJson.put("time", DateUtil.dateToTimeString(dt));
					retJson.put("province", driver.getCity());
					retJson.put("city", driver.getCity());
					retJson.put("product", driver.getProduct());
					errMessage = retJson.toJSONString();
					logger.info("返回结果" + errMessage);
					representation = new StringRepresentation(errMessage);
					return representation;
				}
				if (!"1".equals(status)) {
					retJson.put("status", "6");
					retJson.put("message", "交管所线路出线问题 暂不支持");
					retJson.put("time", DateUtil.dateToTimeString(dt));
					retJson.put("province", driver.getCity());
					retJson.put("city", driver.getCity());
					retJson.put("product", driver.getProduct());
					errMessage = retJson.toJSONString();
					logger.info("返回结果" + errMessage);
					representation = new StringRepresentation(errMessage);
					return representation;
				}

				// 验证驾驶人姓名
				if (!"0".equals(isName)) {
					if (StringUtils.isBlank(driver.getDriver_name())) {
						retJson.put("status", "6");
						retJson.put("message", "请检查姓名");
						retJson.put("time", DateUtil.dateToTimeString(dt));
						retJson.put("province", driver.getCity());
						retJson.put("city", driver.getCity());
						retJson.put("product", driver.getProduct());
						errMessage = retJson.toJSONString();
						logger.info("返回结果" + errMessage);
						representation = new StringRepresentation(errMessage);
						return representation;
					}
				} else {
					// 如果不需要驾驶人姓名 把参数设置为选填
					driver.setDriver_name("选填");
				}

				// 验证驾驶证号
				if (!"0".equals(isLicense)) {
					if (StringUtils.isBlank(driver.getDriver_license()) || !IdcardUtils.validateCard(driver.getDriver_license())) {
						retJson.put("status", "6");
						retJson.put("message", "请检查驾驶证号");
						retJson.put("time", DateUtil.dateToTimeString(dt));
						retJson.put("province", driver.getCity());
						retJson.put("city", driver.getCity());
						retJson.put("product", driver.getProduct());
						errMessage = retJson.toJSONString();
						logger.info("返回结果" + errMessage);
						representation = new StringRepresentation(errMessage);
						return representation;
					}
				} else {
					// 如果不需要 驾驶证号 把参数设置为选填
					driver.setDriver_license("选填");
				}

				// 验证初次领证日期
				if (!"0".equals(isLicensedate)) {
					if (StringUtils.isBlank(driver.getLissue_date())) {
						retJson.put("status", "6");
						retJson.put("message", "请检查初次领证日期");
						retJson.put("time", DateUtil.dateToTimeString(dt));
						retJson.put("province", driver.getCity());
						retJson.put("city", driver.getCity());
						retJson.put("product", driver.getProduct());
						errMessage = retJson.toJSONString();
						logger.info("返回结果" + errMessage);
						representation = new StringRepresentation(errMessage);
						return representation;
					}
				} else {
					// 如果不需要 初次领证日期 把参数设置为选填
					driver.setLissue_date("选填");
				}
				// 验证驾驶证档案编号
				if (!"0".equals(isArchive)) {
					if (!StringUtil.isArchive(driver.getLissue_archive())) {
						retJson.put("status", "6");
						retJson.put("message", "请检查驾驶证档案编号");
						retJson.put("time", DateUtil.dateToTimeString(dt));
						retJson.put("province", driver.getCity());
						retJson.put("city", driver.getCity());
						retJson.put("product", driver.getProduct());
						errMessage = retJson.toJSONString();
						logger.info("返回结果" + errMessage);
						representation = new StringRepresentation(errMessage);
						return representation;
					}
				} else {
					// 如果不需要 驾驶证档案编号 把参数设置为选填
					driver.setLissue_archive("选填");
				}
				// 验证驾驶证是否长期有效
				if (!"0".equals(isEffective)) {
					if (StringUtils.isBlank(driver.getIs_effective())) {
						retJson.put("status", "6");
						retJson.put("message", "请检查驾驶证是否长期有效");
						retJson.put("time", DateUtil.dateToTimeString(dt));
						retJson.put("province", driver.getCity());
						retJson.put("city", driver.getCity());
						retJson.put("product", driver.getProduct());
						errMessage = retJson.toJSONString();
						logger.info("返回结果" + errMessage);
						representation = new StringRepresentation(errMessage);
						return representation;
					}
				} else {
					// 如果不需要 驾驶证是否长期有效 把参数设置为选填
					driver.setIs_effective("选填");
				}
				// 验证驾驶证有效截至日期
				if (!"0".equals(effectiveDate)) {
					if (StringUtils.isBlank(driver.getEffective_date())) {
						retJson.put("status", "6");
						retJson.put("message", "请检查驾驶证有效截至日期");
						retJson.put("time", DateUtil.dateToTimeString(dt));
						retJson.put("province", driver.getCity());
						retJson.put("city", driver.getCity());
						retJson.put("product", driver.getProduct());
						errMessage = retJson.toJSONString();
						logger.info("返回结果" + errMessage);
						representation = new StringRepresentation(errMessage);
						return representation;
					}
				} else {
					// 如果不需要 驾驶证有效截至日期 把参数设置为选填
					driver.setEffective_date("选填");
				}
				st = driverService.getWebJiFen(driver);
			} else {
				retJson.put("status", "6");
				retJson.put("message", "请输入城市");
				retJson.put("time", DateUtil.dateToTimeString(dt));
				retJson.put("province", driver.getCity());
				retJson.put("city", driver.getCity());
				retJson.put("product", driver.getProduct());
				st = retJson.toJSONString();
				logger.info("返回结果" + st);
			}
			representation = new StringRepresentation(st);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return representation;
	}
}
