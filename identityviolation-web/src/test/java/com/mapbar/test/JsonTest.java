package com.mapbar.test;

import org.apache.log4j.Logger;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class JsonTest {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JsonTest.class);

	@Test
	public void test01() {
		JSONObject retJson=new JSONObject();
		retJson.put("status", "");
		retJson.put("message", "城市参数非法");
		System.out.println(retJson.toJSONString());
		logger.info(retJson.toJSONString());
		logger.info(retJson.toString());
	}
}
