package com.mapbar.city.deprecated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.Car100Parser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.ConfigUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class Car100Service implements Transfer {
	private static final String GET_VIOLATION_URL_F = "http://www.qiche100.cn/WZ_Result.aspx?c=0&p=0";
	private static final String GET_VIOLATION_URL = "http://www.qiche100.cn/Handler/WZ_AJAX_Server.aspx";
	private static final String STRING_REFERER = "http://www.qiche100.cn/WZ_Result.aspx?c=0&p=0";
	private static final String ROOT_URL = "http://www.qiche100.cn/";
	Car100Parser parser = new Car100Parser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, ProxyManager.next(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret = ResultCache.toJsonResult("服务维护中, 请稍候再试.", "err");
		try {

			String strData = ConfigUtils.getCitySourceRole(car.getCityPy(), Car100Service.class.getSimpleName());
			System.out.println(strData);
			int isEng = new Integer(strData.split(",")[3]);
			int isBtm = new Integer(strData.split(",")[4]);
			String proName = strData.split(",")[1];
			System.out.println("proName==" + proName);
			String engId = "";
			String btmId = "";
			if (proName.equals("上海") || proName.equals("北京") || proName.equals("重庆") || proName.equals("天津")) {
				proName += "市";
			} else {
				if (proName.equals("广西"))
					proName = "广西壮族自治区";
				if (proName.equals("西藏"))
					proName = "西藏自治区";
				if (!proName.equals("宁夏") && !proName.equals("内蒙古") && !proName.equals("新疆") && !proName.equals("香港")) {
					proName += "省";
				}
			}

			int Loop = 0;
			while (Loop < 3) {
				Loop++;
				try {

					List<String> vCookies = new ArrayList<String>();

					HttpClientUtil.getURLContentsWithCookies(ROOT_URL, vCookies, ROOT_URL, next);
					// Map<String,String> hidden = parser.parseHidden(html, "utf-8");
					// System.out.println("hidden=="+hidden);
					// String hiddenstr=parser.getParamDate(hidden);
					// System.out.println("hiddenstr=="+hiddenstr);
					// String keys = parser.getKeys(html);
					String VinAndEin = "";
					String VElength = "";
					if (isEng != 0) {
						if (!StringUtil.isNotEmpty(engId) || engId.equals("选填")) {
							return ResultCache.toErrJsonResult("车辆信息错误,发动机号为空！");
						}
						if (isEng != -1) {
							if (engId.length() < isEng) {
								return ResultCache.toErrJsonResult("车辆信息错误,发动机号过短！");
							}
							if (engId.length() > isEng) {
								engId = engId.substring(engId.length() - isEng, engId.length());
							}
							VinAndEin += "&VIN=" + engId;
							VElength += "&hid_VIN_length=" + isEng;
						} else {
							VinAndEin += "&VIN=" + engId;
							VElength += "&hid_VIN_length=all";
						}
					} else {
						VElength += "&hid_VIN_length=";
					}
					if (isBtm != 0) {
						if (!StringUtil.isNotEmpty(btmId) || btmId.equals("选填")) {
							return ResultCache.toErrJsonResult("车辆信息错误,车架号为空！");
						}
						if (isBtm != -1) {
							if (btmId.length() < isBtm) {
								return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
							}
							if (btmId.length() > isBtm) {
								btmId = btmId.substring(btmId.length() - isBtm, btmId.length());
							}
							VinAndEin += "&EIN=" + btmId;
							VElength += "&hid_EIN_length=" + isBtm;
						} else {
							VinAndEin += "&EIN=" + btmId;
							VElength += "&hid_EIN_length=all";
						}
					} else {
						VElength += "&hid_EIN_length=";
					}
                  System.out.println(VinAndEin+VElength);
					// Province:北京市
					// City:北京
					// Abbr:京
					// Vehicle:Q93S23
					// FullVehicle:
					// carBuyYear:2014
					// insMonthDue:11
					// VehicleType:01
					// VIN:AB594017
					// hid_VIN:请填写完整发动机号
					// hid_VIN_length:all
					// EIN:
					// hid_EIN:
					// hid_EIN_length:
					// phone:13800138000
					// accept:1
					// keys:aI3p6mSa4v5wR+f9pa9Dlu/yDNwie23Y
					// String postDateFirst="Province="+proName+"&City="+car.strCity+"&Abbr="+car.strState+"&Vehicle="+car.strRegId+
					// "&FullVehicle=&carBuyYear=2014&insMonthDue=10&Phone=13800138000&accept=1"+"&keys="+keys+VinAndEin+VElength;
					// System.out.println("postDateFirst==" + postDateFirst);
					Map<String, String> postDateFirst = new HashMap<String, String>();
					String strRespF = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL_F, postDateFirst, vCookies, STRING_REFERER, "UTF-8", next);
					Map<String, String> hidden = parser.parseHidden(strRespF);
					System.out.println("hidden==" + hidden);
					// String set_vqid = hidden.get("set_vqid") == null ? "" : hidden.get("set_vqid");
					// System.out.println("keys=="+keys);
					// GetMethod:WZQuery
					// keys:aI3p6mSa4v6ft54mCNObe15SjMNE2Y8O
					// set_vcq_City:北京
					// set_vcq_Province:北京市
					// set_vcq_lpn:京P1MB93
					// set_vqid:11320968
					// c:0
					// p:0
					// VehicleType:01
					// Abbr:京
					// Vehicle:P1MB93
					// Province:北京市
					// City:北京
					// VIN:C4NE00134
					// EIN:
					// carBuyYear:2014
					// insMonthDue:10
					// Phone:13800138000
					// accept:1
					// String postData = "";
					// String postData = "GetMethod=WZQuery&keys="+keys+"&set_vcq_City="+car.strCity+"&set_vcq_Province="+proName+"&set_vcq_lpn="+car.strState+car.strRegId+"&set_vqid="+
					// set_vqid+ "&c=0&p=0&VehicleType=01&Abbr="+car.strState+"&Vehicle="+car.strRegId+"&Province="+proName+"&City="+car.strCity;
					// //
					// "&VIN="+(car.strEngId==null?"":(car.strEngId.equals("选填")?"":car.strEngId))+"&EIN="+(car.strBtmId==null?"":(car.strBtmId.equals("选填")?"":car.strBtmId))
					// postData += VinAndEin;
					// postData += "&carBuyYear=2014&insMonthDue=10&Phone=13800138000&accept=1";
					// System.out.println("postData==" + postData);

					// String postData = "GetMethod=WZQuery&keys="+keys+"&set_vcq_City=%E5%8C%97%E4%BA%AC&set_vcq_Province=%E5%8C%97%E4%BA%AC%E5%B8%82&set_vcq_lpn=%E4%BA%ACQ93S23&set_vqid=&c=0&p=0&VehicleType=01&Abbr=%E4%BA%AC&Vehicle=Q93S23&Province=%E5%8C%97%E4%BA%AC%E5%B8%82&City=%E5%8C%97%E4%BA%AC&VIN=AB594017&EIN=&carBuyYear=2015&insMonthDue=10&Phone=13800138000&accept=1";
					Map<String, String> postData = new HashMap<String, String>();
					String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
					// System.out.println("vCookies=="+vCookies);
					System.out.println("strResp==" + strResp);
					LogUtil.doMkLogData_che100(strResp, car);
					if (StringUtil.isNotEmpty(strResp) && strResp.contains("恭喜，您的车辆无违章信息")) {
						return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}
					if (StringUtil.isNotEmpty(strResp) && strResp.contains("\"date\":")) {

						JSONObject jresp = JSONObject.parseObject(strResp);
						String data = jresp.getString("date");
						ret = parser.parse(data, car);

						break;
					}
				} catch (Exception e) {
					LogUtil.doMkLogData_che100("", car);
					e.printStackTrace();
					Thread.sleep(1000);
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ret;
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_che100("", car);
		}
		return ret;
	}
}
