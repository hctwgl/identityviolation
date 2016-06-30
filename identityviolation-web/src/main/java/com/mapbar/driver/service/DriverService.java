package com.mapbar.driver.service;

import java.io.IOException;

import com.mapbar.driver.bean.Driver;

public interface DriverService {

	/**
	 * 驾驶证积分查询接口
	 * 
	 * @param driverName
	 * @param driverLicense
	 * @param lissueDate
	 * @param lissueArchive
	 * @param Province
	 * @param cityJC
	 * @param city_pinyin
	 * @param isEffective
	 * @param product
	 * @param effectiveDate
	 * @param ip
	 * @param refresh
	 * @return
	 * @throws IOException
	 */
	public String getWebJiFen(Driver driver);

	/**
	 * 专门用于绑定用户信息的接口
	 * 
	 * @throws IOException
	 */
	public String saveUser(Driver driver);

	/**
	 * 封装驾驶证查积分接口
	 * 
	 * @param co
	 * @return
	 * @throws IOException
	 */
	public String getDriverScore(Driver driver);
}
