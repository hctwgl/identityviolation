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
import com.mapbar.driver.service.LoginService;
import com.mapbar.driver.utils.StringUtil;

@Controller
@Scope("prototype")
public class LoginController extends BaseController {
	private static final Logger logger = Logger.getLogger(LoginController.class);

	@Resource
	private Driver driver;

	@Resource
	private LoginService loginService;

	@Override
	protected void getParameter(Form form) {
		logger.info("form Login:::" + form);
		for (org.restlet.data.Parameter parameter : form) {
			if ("product".equalsIgnoreCase(parameter.getName()) && !StringUtils.isEmpty(parameter.getValue())) {
				driver.setProduct(StringUtil.trimString(parameter.getValue()));
			} else if ("key".equalsIgnoreCase(parameter.getName()) && !StringUtils.isEmpty(parameter.getValue())) {
				driver.setKey(StringUtil.trimString(parameter.getValue()));
			} else if ("userId".equalsIgnoreCase(parameter.getName()) && !StringUtils.isEmpty(parameter.getValue())) {
				driver.setUserId(StringUtil.trimString(parameter.getValue()));
			} else if ("userToken".equalsIgnoreCase(parameter.getName()) && !StringUtils.isEmpty(parameter.getValue())) {
				driver.setUserToken(StringUtil.trimString(parameter.getValue()));
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
			st = loginService.getLogin(driver);
		} catch (Exception e) {
			e.printStackTrace();
			st = "loginService{\"status\":\"1020\",\"message\":\"保存失败\"}";
		}
		logger.info(st + "@@" + driver.getProduct() + driver.getKey() + driver.getUserId() + driver.getUserToken());
		representation = new StringRepresentation(st);
		return representation;
	}

}
