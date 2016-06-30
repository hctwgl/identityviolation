package com.mapbar.driver.bean;

import java.io.Serializable;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class City implements Serializable{
	private static final long serialVersionUID = 5984138804177310217L;
	private String province_pinyin; // 省份拼音
	private String province; // 省份名称
	private String pro; // 省份简称
	private String city_code; // 城市编码
	private String city_name; // 城市名称
	private String city_status; // 城市上下线
	private int isyzm;// 是否需要图片验证码
	private String is_name;// 是否需要驾驶人姓名
	private String is_license;// 是否需要驾驶证号
	private String is_licensedate;// 是否需要初次领证日期
	private String is_archive;// 是否需要驾驶证档案编号
	private String is_effective;// 是否需要驾驶证是否长期有效
	private String effective_date;// 是否需要驾驶证有效截至日期
	private String update_time = "";// 城市列表更新时间

	public City() {
	}
	public String getProvince_pinyin() {
		return province_pinyin;
	}

	public void setProvince_pinyin(String province_pinyin) {
		this.province_pinyin = province_pinyin;
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

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getCity_status() {
		return city_status;
	}

	public void setCity_status(String city_status) {
		this.city_status = city_status;
	}

	public int getIsyzm() {
		return isyzm;
	}

	public void setIsyzm(int isyzm) {
		this.isyzm = isyzm;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
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

}
