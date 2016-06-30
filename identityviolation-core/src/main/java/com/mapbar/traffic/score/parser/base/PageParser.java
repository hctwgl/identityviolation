package com.mapbar.traffic.score.parser.base;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;

public interface PageParser {
	
	public void parse(String result, JSONObject jobj, DriverProfile driverProfile);
}
