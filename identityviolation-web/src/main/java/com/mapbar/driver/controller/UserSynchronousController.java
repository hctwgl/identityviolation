package com.mapbar.driver.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mapbar.driver.bean.Driver;
import com.mapbar.driver.service.UserSynchronousService;
import com.mapbar.driver.utils.StringUtil;

@Controller
@Scope("prototype")
public class UserSynchronousController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(UserSynchronousController.class);

	@Resource
	private Driver driver;

	@Resource
	private UserSynchronousService userSynchronousService;

	@Override
	protected void getParameter(Form form) {
		logger.info("form UserSynchronous:::" + form);
		for (org.restlet.data.Parameter parameter : form) {
			if ("product".equalsIgnoreCase(parameter.getName()) && !StringUtils.isEmpty(parameter.getValue())) {
				driver.setProduct(StringUtil.trimString(parameter.getValue()));
			}
			if ("json".equalsIgnoreCase(parameter.getName()) && !StringUtils.isEmpty(parameter.getValue())) {
				driver.setJson(StringUtil.trimString(parameter.getValue()));
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
		Representation representation = null;
		try {
			st = userSynchronousService.getUserSynchronous(driver);
		} catch (Exception e) {
			e.printStackTrace();
			st = "{\"status\":\"1020\",\"message\":\"保存失败\"}";
		}
		logger.info("UserSynchronousLog" + driver.getProduct() + driver.getJson() + "@@" + st);
		representation = new StringRepresentation(st);
		return representation;
	}

}
