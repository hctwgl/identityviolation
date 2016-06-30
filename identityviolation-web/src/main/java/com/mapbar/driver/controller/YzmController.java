package com.mapbar.driver.controller;

import org.apache.commons.lang3.StringUtils;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.relay.RelayProcess;

@Controller
@Scope("prototype")
public class YzmController extends BaseController {
	private String sid = "";
	private String cs = "";

	@Override
	protected void getParameter(Form form) {
		for (org.restlet.data.Parameter parameter : form) {
			if ("sid".equalsIgnoreCase(parameter.getName()) && !StringUtils.isEmpty(parameter.getValue())) {
				sid = parameter.getValue();
			} else if ("cs".equalsIgnoreCase(parameter.getName())) {
				cs = parameter.getValue();
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

			String json = RelayProcess.getInstance().prcessRefreshYzm(sid, cs);
			JSONObject job = JSONObject.parseObject(json);
			JSONObject ret = new JSONObject();
			if ("ok".equals(job.get("status"))) {
				ret.put("status", "0");
			} else {
				ret.put("status", job.get("status"));
			}
			if (job.get("msg") != null) {
				ret.put("message", job.get("msg"));
			}
			if (job.containsKey("extra_expand_info")) {
				ret.put("extra_expand_info", job.get("extra_expand_info"));
			}
			representation = new StringRepresentation(ret.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return representation;

	}

}
