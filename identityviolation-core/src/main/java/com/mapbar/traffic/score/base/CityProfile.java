package com.mapbar.traffic.score.base;

import java.io.Serializable;

public class CityProfile implements Serializable{
	private static final long serialVersionUID = 9154579666536256018L;
	private String province = null;// 省份名称
	private String pro = null;// 省份简称
	private String cityName = null;// 城市代码
	private String cityPy = null;// 城市拼音
	private int isyzm;// 是否需要图片验证码
	private String is_name;// 是否需要驾驶人姓名
	private String is_license;// 是否需要驾驶证号
	private String is_licensedate;// 是否需要初次领证日期
	private String is_archive;// 是否需要驾驶证档案编号
	private String is_effective;// 是否需要驾驶证是否长期有效
	private String effective_date;// 是否需要驾驶证有效截至日期
	private String update_time = "";// 城市列表更新时间
	private String proPy;// 省份拼音

	public CityProfile() {
	}

	public String toString() {
		return this.cityName + "," + this.province + "," + this.pro + "," + this.cityPy;
	}

	public int getIsyzm() {
		return isyzm;
	}

	public void setIsyzm(int isyzm) {
		this.isyzm = isyzm;
	}

	public String getIs_name() {
		return is_name;
	}

	public void setIs_name(String is_name) {
		this.is_name = is_name;
	}

	public String getIs_license() {
		return is_license;
	}

	public void setIs_license(String is_license) {
		this.is_license = is_license;
	}

	public String getIs_licensedate() {
		return is_licensedate;
	}

	public void setIs_licensedate(String is_licensedate) {
		this.is_licensedate = is_licensedate;
	}

	public String getIs_archive() {
		return is_archive;
	}

	public void setIs_archive(String is_archive) {
		this.is_archive = is_archive;
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

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getProPy() {
		return proPy;
	}

	public void setProPy(String proPy) {
		this.proPy = proPy;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
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
}
