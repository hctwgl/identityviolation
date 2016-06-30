package com.mapbar.driver.dao;

import java.util.List;

import com.mapbar.driver.bean.City;

public interface CityDao {
	/**
	 * 获取省份拼音列表
	 */
	public List<String> getProPyList(String sql);

	/**
	 * 获取城市信息
	 */
	public List<City> getCityList(String sql);

	/**
	 * 得到城市的最后一次更新时间
	 * 
	 * @return
	 */
	public String getMaxUpdateTime();
}
