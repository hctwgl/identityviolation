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

@Controller
@Scope("prototype")
public class CityListController extends BaseController {
	@Resource
	private Driver driver;

	@Resource
	private CityService cityService;

	public static String CityList = "";

	@Override
	protected void getParameter(Form form) {
		for (org.restlet.data.Parameter parameter : form) {
			if (null != parameter.getName() && "jsonpcallback".equalsIgnoreCase(parameter.getName())) {
				driver.setJsonpcallback(parameter.getValue());
			}
			if (null != parameter.getName() && "refresh".equalsIgnoreCase(parameter.getName())) {
				driver.setRefresh(parameter.getValue());
			}
		}
	}

	@Override
	protected Representation getXMLRepresentation() {
		return null;
	}

	@Override
	protected Representation getJSONRepresentation() {
		Representation representation = null;
		String reStr = "";
		try {
			// 是否更新数据
			if (StringUtils.isEmpty(CityList) || "1".equals(driver.getRefresh())) {
				CityList = cityService.getCityList();
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
