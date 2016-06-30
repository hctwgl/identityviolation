package com.mapbar.traffic.score.logs;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.utils.StringUtil;

public class LogUtil {

	public static String SOHU_SOURCE_NAME = "sohu";
	public static String CHESHOUYE_SOURCE_NAME = "cheshouye";
	public static String CHE_100_SOURCE_NAME = "che100";
	public static String CAR_HOME_SOURCE_NAME = "carhome";

	public static Map<String, String> sourceCNMap = new HashMap<String, String>();
	public static String WECAR_SOURCE_NAME = "wecar";

	static {
		sourceCNMap.put(SOHU_SOURCE_NAME, "搜狐");
		sourceCNMap.put(WECAR_SOURCE_NAME, "微车");
		sourceCNMap.put(CHE_100_SOURCE_NAME, "车100");
		sourceCNMap.put(CHESHOUYE_SOURCE_NAME, "车首页");
		sourceCNMap.put(CAR_HOME_SOURCE_NAME, "汽车之家");
	}

	/**
	 * 记录交管局查询日志
	 * 
	 * @param driverProfile
	 * @param isOk
	 */
	public static void doMkLogData_jiaoguanProv(DriverProfile driverProfile, String isOk) {
		SourceLogBean bean = new SourceLogBean();
		bean.setResult("");
		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(driverProfile.getProvinceName() + "省交管网");
		bean.setRequstStatus(isOk);
		LogTaskQueue.getInstance().addTask(bean);
	}

	/**
	 * 记录交管局查询日志
	 * 
	 * @param driverProfile
	 * @param isOk
	 */
	public static void doMkLogData_JGUProv_With_Msg(DriverProfile driverProfile, String isOk, String msg) {
		SourceLogBean bean = new SourceLogBean();
		bean.setResult("");
		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(driverProfile.getProvinceName() + "省交管网");
		bean.setRequstStatus(isOk);
		LogTaskQueue.getInstance().addTask(bean);
	}

	/**
	 * 记录交管局查询日志
	 * 
	 * @param driverProfile
	 * @param isOk
	 */
	public static void doMkLogData_jiaoguanju(DriverProfile driverProfile, String isOk) {
		SourceLogBean bean = new SourceLogBean();
		bean.setResult("");
		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(driverProfile.getProvinceName() + "交管网");
		bean.setRequstStatus(isOk);
		LogTaskQueue.getInstance().addTask(bean);
	}

	/**
	 * 记录交管局查询日志
	 * 
	 * @param car
	 * @param isOk
	 */
	public static void doMkLogData_JGU_With_Msg(DriverProfile driverProfile, String isOk, String msg) {
		SourceLogBean bean = new SourceLogBean();
		bean.setResult("");
		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(driverProfile.getProvinceName() + "交管网");
		bean.setRequstStatus(isOk);
		LogTaskQueue.getInstance().addTask(bean);
	}

	public static void doMkLogData_cheshouye(String strResp, DriverProfile driverProfile, String isOk) {
		SourceLogBean bean = new SourceLogBean();
		bean.setResult(strResp);
		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(CHESHOUYE_SOURCE_NAME);
		bean.setRequstStatus(isOk);
		LogTaskQueue.getInstance().addTask(bean);
	}

	public static void doMkLogData_sohu(DriverProfile driverProfile, String isOk, String msg) {
		SourceLogBean bean = new SourceLogBean();
		bean.setResult(msg);
		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(sourceCNMap.get(SOHU_SOURCE_NAME) + ":" + driverProfile.getCityName());
		bean.setRequstStatus(isOk);
		LogTaskQueue.getInstance().addTask(bean);
	}

	public static void doMkLogData_wecar(DriverProfile driverProfile, String isOk, String msg) {
		SourceLogBean bean = new SourceLogBean();
		bean.setResult(msg);
		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(sourceCNMap.get(WECAR_SOURCE_NAME) + ":" + driverProfile.getCityName());
		bean.setRequstStatus(isOk);
		LogTaskQueue.getInstance().addTask(bean);
	}

	public static void doMkLogData_wecar(String strResp, DriverProfile driverProfile) {
		SourceLogBean bean = new SourceLogBean();

		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(sourceCNMap.get(WECAR_SOURCE_NAME) + ":" + driverProfile.getCityName());
		if (strResp != null && strResp.contains("error")) {
			Document doc = Jsoup.parse(strResp);
			bean.setResult(doc.text());
			bean.setRequstStatus("err");
		} else if (strResp != null && !strResp.contains("error") && strResp.contains("\"vehicle_status\":\"ok\"")) {
			Document doc = Jsoup.parse(strResp);
			bean.setResult(doc.text());
			bean.setRequstStatus("err");
		} else {
			bean.setRequstStatus("err");
		}
		LogTaskQueue.getInstance().addTask(bean);
	}

	public static void doMkLogData_che100(String strResp, DriverProfile driverProfile) {
		SourceLogBean bean = new SourceLogBean();

		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(CHE_100_SOURCE_NAME);
		if (StringUtil.isNotEmpty(strResp) && strResp.contains("\"date\":")) {
			bean.setRequstStatus("ok");
		} else {
			bean.setRequstStatus("err");
		}
		LogTaskQueue.getInstance().addTask(bean);
	}

	public static void doMkLogData_carhome(String strResp, DriverProfile driverProfile) {
		SourceLogBean bean = new SourceLogBean();

		bean.setCityName(driverProfile.getCityName());
		bean.setCityPy(driverProfile.getCityPy());
		bean.setLssueArchive(driverProfile.getLssueArchive());
		bean.setDriverLicense(driverProfile.getDriverLicense());
		bean.setDriverName(driverProfile.getDriverName());
		bean.setLssueDate(driverProfile.getLssueDate());
		bean.setProvince(driverProfile.getProvinceName());
		bean.setSource(sourceCNMap.get(CAR_HOME_SOURCE_NAME) + ":" + driverProfile.getCityName());
		if (StringUtil.isNotEmpty(strResp) && strResp.contains("\"returncode\":0")) {
			bean.setRequstStatus("ok");
		} else {
			bean.setRequstStatus("err");
		}
		LogTaskQueue.getInstance().addTask(bean);
	}
}
