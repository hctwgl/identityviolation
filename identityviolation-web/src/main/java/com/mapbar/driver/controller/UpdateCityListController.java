package com.mapbar.driver.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mapbar.driver.bean.Driver;
import com.mapbar.driver.service.CityService;
import com.mapbar.driver.utils.DateUtil;

@Controller
@Scope("prototype")
public class UpdateCityListController extends BaseController {
	@Resource
	private Driver driver;
	@Resource
	private CityService cityService;

	@Override
	protected void getParameter(Form form) {
		System.out.println(form);
		for (org.restlet.data.Parameter parameter : form) {
			if (null != parameter.getName() && "jsonpcallback".equalsIgnoreCase(parameter.getName())) {
				driver.setJsonpcallback(parameter.getValue());
			}
			if (null != parameter.getName() && "refresh".equalsIgnoreCase(parameter.getName())) {
				driver.setRefresh(parameter.getValue());
			}
			// if ("update_time".equalsIgnoreCase(parameter.getName()) && !StringUtil.isEmpty(parameter.getValue())) {
			if ("update_time".equalsIgnoreCase(parameter.getName())) {
				if (StringUtils.isBlank(parameter.getValue())) {
					driver.setUpdate_time(null);
				} else {
					driver.setUpdate_time(parameter.getValue());
				}
			}
		}
	}

	@Override
	protected Representation getXMLRepresentation() {
		return null;
	}

	public static String CityList = "";

	@Override
	protected Representation getJSONRepresentation() {
		Representation representation = null;
		String reStr = "";
		try {
			if ((!"".equals(driver.getUpdate_time()) && DateUtil.isValidDate(driver.getUpdate_time())) || driver.getUpdate_time() == null) {
				CityList = cityService.getUpdateCityList(driver.getUpdate_time());
			} else {
				reStr = "{\"status\":\"3\",\"message\":\"请检查参数\"}";
				representation = new StringRepresentation(reStr);
				return representation;
			}
			// 是否更新数据
			if (StringUtils.isEmpty(CityList) || "1".equals(driver.getRefresh())) {
				CityList = cityService.getUpdateCityList(driver.getUpdate_time());
			}
			// jsonp 返回
			if (StringUtils.isEmpty(driver.getJsonpcallback())) {
				reStr = CityList;
			} else {
				reStr = driver.getJsonpcallback() + "(" + CityList + ")";
			}

			representation = new StringRepresentation(reStr);
		} catch (Exception e) {
			representation = new StringRepresentation("error");
		}
		return representation;
	}

}
