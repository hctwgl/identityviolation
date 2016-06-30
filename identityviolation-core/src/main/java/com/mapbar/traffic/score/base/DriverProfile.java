package com.mapbar.traffic.score.base;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DriverProfile implements Serializable {

	private static final long serialVersionUID = 2138617416406614114L;
	private String driverName = "";// 驾驶人姓名
	private String driverLicense = "";// 驾驶证号
	private String lssueDate = "";// 初次领证日期
	private String lssueArchive = "";// 驾驶证档案编号
	private String is_effective = "";// 驾驶证是否长期有效
	private String effective_date = "";// 驾驶证有效截止日期
	private String cityName = "";
	private String cityPy = "";
	private String isShowYzm = "0";
	private boolean refresh = false;
	private String className = "";
	private String session = "";
	private String valCode = "";
	private String pro = "";// 省份简称

	public DriverProfile() {
	}

	public DriverProfile set(String driverName, String driverLicense, String lssueDate, String lssueArchive, String is_effective, String effective_date) {
		this.driverName = driverName;
		this.driverLicense = driverLicense;
		this.lssueDate = lssueDate;
		this.lssueArchive = lssueArchive;
		this.is_effective = is_effective;
		this.effective_date = effective_date;

		return this;
	}

	public DriverCase vc = new DriverCase();

	public String getProvinceName() {
		return ServiceConfig.getCityProfile(cityPy).getProvince();
	}

	// province=%E5%AE%89%E5%BE%BD&city_pinyin=hefei&car_province=%E7%9A%96&license_plate_num=AL2M38&body_num=216662&engine_num=%E9%80%89%E5%A1%AB
	public String toWecarString() {
		try {
			return "province=" + URLEncoder.encode(getProvinceName(), "utf-8") + "&city_pinyin=" + cityPy + "&car_province=" + URLEncoder.encode(this.driverName, "utf-8") + "&license_plate_num=" + URLEncoder.encode(driverName, "utf-8") + "&body_num=" + URLEncoder.encode(driverName, "utf-8") + "&engine_num=" + URLEncoder.encode(driverName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String toWecheString() {
		try {
			return "province=" + URLEncoder.encode(getProvinceName(), "UTF-8") + "&pinyin=" + cityPy + "&car_province=" + URLEncoder.encode(driverName, "UTF-8") + "&license_plate_num=" + URLEncoder.encode(driverName, "UTF-8") + "&body_num=" + URLEncoder.encode(driverName, "UTF-8") + "&engine_num=" + URLEncoder.encode(driverName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String toString() {
		return "province=" + getProvinceName() + "&city=" + cityName + "&city_pinyin=" + cityPy;
	}

	/**
	 * 驾驶证号和驾驶证编号唯一
	 * 
	 */
	public String toCacheKey() {
		return this.cityPy + this.driverLicense;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}

	public String getLssueDate() {
		return lssueDate;
	}

	public void setLssueDate(String lssueDate) {
		this.lssueDate = lssueDate;
	}

	public String getLssueArchive() {
		return lssueArchive;
	}

	public void setLssueArchive(String lssueArchive) {
		this.lssueArchive = lssueArchive;
	}

	public String getIs_effective() {
		return is_effective;
	}

	public void setIs_effective(String is_effective) {
		this.is_effective = is_effective;
	}

	public String getEffective_date() {
		return effective_date;
	}

	public void setEffective_date(String effective_date) {
		this.effective_date = effective_date;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityPy() {
		return cityPy;
	}

	public void setCityPy(String cityPy) {
		this.cityPy = cityPy;
	}

	public String getIsShowYzm() {
		return isShowYzm;
	}

	public void setIsShowYzm(String isShowYzm) {
		this.isShowYzm = isShowYzm;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getValCode() {
		return valCode;
	}

	public void setValCode(String valCode) {
		this.valCode = valCode;
	}

	public DriverCase getVc() {
		return vc;
	}

	public void setVc(DriverCase vc) {
		this.vc = vc;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

}
