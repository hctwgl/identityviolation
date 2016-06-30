package com.mapbar.traffic.score.logs;

import java.io.Serializable;
import java.util.Calendar;

public class SourceLogBean implements Serializable {

	private static final long serialVersionUID = 6791927859578490442L;
	private String province = "";
	private String pro = "";
	private String cityName = "";
	private String cityPy = "";
	private String source = "";
	// ok or err
	private String requstStatus = "ok";
	private String result = "";

	private String driverName = "";// 驾驶人姓名
	private String driverLicense = "";// 驾驶证号
	private String lssueDate;// 初次领证日期
	private String lssueArchive;// 驾驶证档案编号
	private String is_effective;// 驾驶证是否长期有效
	private String effective_date;// 驾驶证有效截止日期

	public SourceLogBean() {
	}

	public String toInsertSql() {
		String sql = "insert into source_request_log_" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + " (province,pro,city,citypy,source,driver_name,driver_license,lissue_date,lissue_archive,is_effective,status,result,effective_date,req_time) values (" + "'" + (province == null ? "" : province) + "'," + "'" + (pro == null ? "" : pro) + "'," + "'" + (cityName == null ? "" : cityName) + "'," + "'" + (cityPy == null ? "" : cityPy)
				+ "'," + "'" + (source == null ? "" : source) + "'," + "'" + (driverName == null ? "" : driverName) + "'," + "'" + (driverLicense == null ? "" : driverLicense) + "'," + "'" + (lssueDate == null ? "" : lssueDate) + "'," + "'" + (lssueArchive == null ? "" : lssueArchive) + "'," + "'" + (is_effective == null ? "" : is_effective) + "'," + "'" + (requstStatus == null ? "" : requstStatus) + "'," + "'"
				+ (result == null ? "" : result) + "'," + "'" + (effective_date == null ? "" : effective_date) + "',now())";
		return sql;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRequstStatus() {
		return requstStatus;
	}

	public void setRequstStatus(String requstStatus) {
		this.requstStatus = requstStatus;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

}
