package com.mapbar.driver.bean;

import java.io.Serializable;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Driver implements Serializable {
	private static final long serialVersionUID = -5848235974318148729L;
	private String province = "";// 省份名称
	private String pro = "";// 省份简称
	private String city_pinyin = "";// 城市拼音
	private String city = ""; // 城市名称
	private String driver_name = "";// 驾驶人姓名
	private String driver_license = "";// 驾驶证号 15或者18位身份证号码
	private String lissue_date = "";// 初次领证日期
	private String lissue_archive = "";// 驾驶证档案编号10位或者12位数字
	private String is_effective = ""; // 驾驶证是否长期有效
	private String effective_date = ""; // 驾驶证有效截止日期
	private String pushSwitch = ""; // 推送开关
	private String userId = ""; // 用户登录id
	private String userToken = ""; // 用户登录有效验证
	private String key = ""; // 设备唯一标识（uuid）
	private String sid = "";// 图片验证码session_id
	private String cs = "";// 处理类class名称
	private String vcode;
	private String product = ""; // 渠道
	private int isyzm = 0;
	private String update_time = "";// 最后更新时间
	private String refresh = "";// 刷新memcached
	private String json = ""; // jsonKey
	private String jsonpcallback = "";// jsonp专用
	private String ip = "";// ip地址
	private String phone_imei = "";// 手机标识
	private String phone_model = "";// 手机类型
	private String phone_system = "";// 手机系统

	public Driver() {
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity_pinyin() {
		return city_pinyin;
	}

	public void setCity_pinyin(String city_pinyin) {
		this.city_pinyin = city_pinyin;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDriver_name() {
		return driver_name;
	}

	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	public String getDriver_license() {
		return driver_license;
	}

	public void setDriver_license(String driver_license) {
		this.driver_license = driver_license;
	}

	public String getLissue_date() {
		return lissue_date;
	}

	public void setLissue_date(String lissue_date) {
		this.lissue_date = lissue_date;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getLissue_archive() {
		return lissue_archive;
	}

	public void setLissue_archive(String lissue_archive) {
		this.lissue_archive = lissue_archive;
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

	public String getPushSwitch() {
		return pushSwitch;
	}

	public void setPushSwitch(String pushSwitch) {
		this.pushSwitch = pushSwitch;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getKey() {
		return key;
	}

	public String getPhone_imei() {
		return phone_imei;
	}

	public void setPhone_imei(String phone_imei) {
		this.phone_imei = phone_imei;
	}

	public String getPhone_model() {
		return phone_model;
	}

	public void setPhone_model(String phone_model) {
		this.phone_model = phone_model;
	}

	public String getPhone_system() {
		return phone_system;
	}

	public void setPhone_system(String phone_system) {
		this.phone_system = phone_system;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
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

	public String getRefresh() {
		return refresh;
	}

	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}

	public String getJsonpcallback() {
		return jsonpcallback;
	}

	public void setJsonpcallback(String jsonpcallback) {
		this.jsonpcallback = jsonpcallback;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

}
