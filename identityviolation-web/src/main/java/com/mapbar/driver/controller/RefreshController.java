package com.mapbar.driver.controller;

import org.apache.log4j.Logger;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mapbar.traffic.score.utils.ConfigUtils;

@Controller
@Scope("prototype")
public class RefreshController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(RefreshController.class);

	@Override
	protected void getParameter(Form form) {
		logger.info("form query:::" + form);
	}

	@Override
	protected Representation getXMLRepresentation() {
		return null;
	}

	@Override
	protected Representation getJSONRepresentation() {
		String st = "成功";
		Representation representation = null;
		try {
			ConfigUtils.refresh();
			representation = new StringRepresentation(st);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return representation;
	}

}
