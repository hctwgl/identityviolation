package com.mapbar.driver.controller;

import org.apache.commons.lang3.StringUtils;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mapbar.traffic.score.utils.ConfigUtils;
@Controller
@Scope("prototype")
public class SourceConfigController extends BaseController {

	private String className = "";
	private String cityPy = "";
	private String usable = "";
	private String isrefresh = "";

	@Override
	protected void getParameter(Form form) {
		for (org.restlet.data.Parameter parameter : form) {
			if ("classname".equalsIgnoreCase(parameter.getName()) && !StringUtils.isEmpty(parameter.getValue())) {
				className = parameter.getValue();
			} else if ("citypy".equalsIgnoreCase(parameter.getName())) {
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
		try {

			String msg = "";
			if (StringUtils.isEmpty(className) && StringUtils.isEmpty(isrefresh)) {
				representation = new StringRepresentation("参数有误,类名与是否刷新配置 不能同时为null");
			}
			if (!StringUtils.isEmpty(className)) {
				if ("1".equals(usable) || "0".equals(usable)) {
					int n = ConfigUtils.updateConfig(cityPy, className, usable);
					if (n > 0) {
						msg = " 配置成功 ";
					} else {
						return new StringRepresentation("配置失败");
					}
				} else {
					return new StringRepresentation("参数有误,是否可用参数只能传 0 or 1");
				}
			}

			if ("1".equals(isrefresh)) {
				ConfigUtils.refresh();
				msg += " 刷新成功 ";
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
