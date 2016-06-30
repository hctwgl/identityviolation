package com.mapbar.driver.utils;

import org.apache.commons.lang3.StringUtils;

import com.mapbar.driver.bean.Driver;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.relay.RelayProcess;

/**
 * 获取到查询的结果数据
 * 
 */
public class PostDriver {

	/**
	 * 
	 * @param driverName
	 * @param driverLicense
	 * @param lissueDate
	 * @param lissueArchive
	 * @param province
	 * @param city_pinyin
	 * @param isEffective
	 * @param sid
	 * @param cs
	 * @param isyzm
	 * @param effectiveDate
	 * @param vcode
	 * @param refresh
	 * @return
	 */
	public static String getDriverScore(Driver driver) {
		RelayProcess process = RelayProcess.getInstance();
		DriverProfile driverProfile = new DriverProfile();
		driverProfile.setClassName(driver.getCs());
		driverProfile.setIsShowYzm(String.valueOf(driver.getIsyzm()));
		driverProfile.setSession(driver.getSid());
		driverProfile.setValCode(driver.getVcode());
		driverProfile.setRefresh("1".equals(driver.getRefresh()));

		driverProfile.setDriverName(driver.getDriver_name());
		driverProfile.setDriverLicense(driver.getDriver_license());
		driverProfile.setLssueDate(driver.getLissue_date());
		driverProfile.setLssueArchive(driver.getLissue_archive());
		driverProfile.setPro(driver.getPro());
		driverProfile.setCityName(driver.getCity());
		driverProfile.setCityPy(driver.getCity_pinyin());
		driverProfile.setIs_effective(driver.getIs_effective());
		driverProfile.setEffective_date(driver.getEffective_date());
		String retStr = process.process(driverProfile);
		if (StringUtils.isBlank(retStr)) {
			retStr = "{\"status\":\"err\",\"msg\":\"查询失败，请稍后再试\"}";
		}
		return retStr;
	}

	public static String getViolationForPush(Driver driver) {
		RelayProcess process = RelayProcess.getInstance();
		DriverProfile driverProfile = new DriverProfile();
		driverProfile.setClassName(driver.getCs());
		driverProfile.setIsShowYzm(String.valueOf(driver.getIsyzm()));
		driverProfile.setSession(driver.getSid());
		driverProfile.setValCode(driver.getVcode());
		driverProfile.setRefresh("1".equals(driver.getRefresh()));

		driverProfile.setDriverName(driver.getDriver_name());
		driverProfile.setDriverLicense(driver.getDriver_license());
		driverProfile.setLssueDate(driver.getLissue_date());
		driverProfile.setLssueArchive(driver.getLissue_archive());
		driverProfile.setPro(driver.getPro());
		driverProfile.setCityName(driver.getCity());
		driverProfile.setCityPy(driver.getCity_pinyin());
		driverProfile.setIs_effective(driver.getIs_effective());
		driverProfile.setEffective_date(driver.getEffective_date());
		String HTML = process.processPush(driverProfile);
		if (HTML == null || "".equals(HTML)) {
			HTML = "{\"status\":\"err\",\"msg\":\"查询失败，请稍后再试\"}";
		}

		return HTML;
	}

}
