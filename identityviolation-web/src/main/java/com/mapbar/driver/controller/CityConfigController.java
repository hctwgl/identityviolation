package com.mapbar.driver.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mapbar.driver.service.CityService;
import com.mapbar.traffic.score.utils.ConfigUtils;

@Controller
@Scope("prototype")
public class CityConfigController extends BaseController {
	private String cityPy = "";
	private String usable = "";
	private String isrefresh = "";
	@Resource
	private CityService cityService;

	@Override
	protected void getParameter(Form form) {
		for (org.restlet.data.Parameter parameter : form) {
			if ("citypy".equalsIgnoreCase(parameter.getName())) {
				cityPy = parameter.getValue();
			} else if ("usable".equalsIgnoreCase(parameter.getName())) {
				usable = parameter.getValue();
			} else if ("isrefresh".equalsIgnoreCase(parameter.getName())) {
				isrefresh = parameter.getValue();
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
		String msg = "";
		try {
			if ("1".equals(isrefresh)) {
				cityService.getCityMap();
				CityListController.CityList = cityService.getCityList();
				UpdateCityListController.CityList=cityService.getUpdateCityList("");
				return new StringRepresentation("刷新城市信息成功");
			}
			if (StringUtils.isEmpty(cityPy)) {
				return new StringRepresentation("参数有误,城市上下线操作时，城市拼音不能为空");
			}
			if ("1".equals(usable) || "0".equals(usable)) {
				int n = ConfigUtils.updateCity(cityPy, usable);
				if (n > 0) {
					cityService.getCityMap();
					CityListController.CityList = cityService.getCityList();
					UpdateCityListController.CityList=cityService.getUpdateCityList("");
					msg = " 配置成功 ";
				} else {
					return new StringRepresentation("配置失败");
				}
			} else {
				return new StringRepresentation("参数有误,城市上下线操作时,是否可用参数只能传 0 or 1");
			}

			if (representation == null) {
				return new StringRepresentation(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return representation;

	}

}
