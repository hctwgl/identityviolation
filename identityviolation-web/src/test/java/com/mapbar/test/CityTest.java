package com.mapbar.test;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.mapbar.driver.service.impl.CityServiceImpl;

public class CityTest {
	private static final Logger logger = Logger.getLogger(CityTest.class);

	@Test
	public void test() {

		//ApplicationContext ctx = new FileSystemXmlApplicationContext("WebContent/WEB-INF/restlet-servlet.xml");// 载入spring配置文件
		//DriverDaoImpl dao = (DriverDaoImpl) ctx.getBean("couponsDao");// 载入server bean
		CityServiceImpl clist = new CityServiceImpl();
		// clist.cityDao(dao);
		String wxCityList = clist.getCityList();
		logger.info(wxCityList);
		// logger.info(clist.getDaoCityList());
		logger.info("无序遍历结果：");
		JSONObject jsonObj = JSON.parseObject(wxCityList);
		for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
			logger.info(entry.getKey() + ":" + entry.getValue());
		}

		logger.info("-------------------");
		logger.info("有序遍历结果1：");
		LinkedHashMap<String, String> jsonMap = JSON.parseObject(wxCityList, new TypeReference<LinkedHashMap<String, String>>() {
		});
		for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
			logger.info(entry.getKey() + ":" + entry.getValue());
		}
		logger.info("----------------------------------------------");
		logger.info("有序遍历结果2：");
		JSONObject parseObject = JSONObject.parseObject(wxCityList, Feature.OrderedField);
		for (Map.Entry<String, Object> entry : parseObject.entrySet()) {
			logger.info(entry.getKey() + ":" + entry.getValue());
		}
		logger.info(parseObject.toString());

	};
}
