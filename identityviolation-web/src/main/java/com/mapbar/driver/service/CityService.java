package com.mapbar.driver.service;

import java.util.HashMap;
import java.util.Map;

import com.mapbar.driver.bean.City;

public interface CityService {

	public static Map<String, City> cityMap = new HashMap<String, City>();

	public static Map<String, String> oldCLMap = new HashMap<String, String>();

	Map<String, City> getCityMap();

	Map<String, String> getOldCityListMap();

	String getCityList();

	String getUpdateCityList(String update_time);

}
