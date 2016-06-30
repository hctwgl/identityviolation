package com.mapbar.provincetodo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.SohuParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.ConfigUtils;
import com.mapbar.traffic.score.utils.HttpsProxyManager;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class SohuService implements Transfer {
	protected static String strProxyFile = System.getProperty("PROXY_FILE", "https_proxy.txt");
	protected static HttpsProxyManager pMgr = new HttpsProxyManager(strProxyFile);
	private static final String STRING_SERVICE_URL = "http://mobile.auto.sohu.com/wzcx/common/api/queryByCity.at?appKey=pc";
	public static final String STRING_REFERER = "http://mobile.auto.sohu.com/wzcx/mainPage.at";
	// public static String STRING_REFERER = "http://mobile.auto.sohu.com/wzcx/index.at";
	private static SohuParser parser = new SohuParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = ""; // ResultCache.toJsonResult("服务维护中, 请稍候再试.");

		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, pMgr.next(true));
				// strResult = lookupViolation(car, null);
			}
			// 读取配置文件 用城市py取 搜索条件
			// SohuConfig sohu = SohuConfig.getServiceConfig(car.strCityPy);

			// if(sohu != null)
			// {

			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	public static String lookupViolation(DriverProfile car, Proxy proxy) {

		String ret = null;
		try {
			// if(sc != null && car != null)
			// {
			// 从数组的第1个元素取查询条件
			String strData = ConfigUtils.getCitySourceRole(car.getCityPy(), SohuService.class.getSimpleName());

			// String strData ="province=110000&city=110000&carNum=京%regid%&ecarBelong=11&ecarPart=%regid%&ecarType=02&engineNum=%engid%";
			// strData="province=440000&city=440300&carNum=粤%regid%&ecarBelong=44&ecarPart=%regid%&ecarType=02&engineNum=%engid%&evin=%btmid%";
			// strData="province=110000&city=110000&carNum=京%regid%&ecarBelong=11&ecarPart=%regid%&ecarType=02&engineNum=%engid%";
			// "province=510000&city=510100&carNum=川%regid%&ecarBelong=51&ecarPart=%regid%&ecarType=02&evin=%btmid%";
			// "province=山东&city_pinyin=qingdao&car_province=鲁&license_plate_num=B899J8&body_num=0057&engine_num=选填&city=青岛";
			//
			strData = strData.replace("ecarType=02", "ecarType=" + car.getCityPy());
			String strUrl = STRING_SERVICE_URL;

			Vector<String> vCookies = new Vector<String>();

			int Loop = 0;

			if (StringUtil.isNotEmpty(strData)) {
				HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, proxy);
				// System.out.println(vCookies);
				// 将查询条件替换到参数中
				// sohu_yzm=&province=510000&city=510100&carNum=川AEF250&ecarBelong=51&ecarPart=AEF250&ecarType=02&evin=WBACN210500R88523
				// sohu_yzm=&province=510000&city=510100&carNum=%E5%B7%9DAEF250&ecarBelong=51&ecarPart=AEF250&ecarType=02&evin=WBACN210500R88523
				// strData = replaceToken(strData, DriverProfile.KEY_REGID, car.strRegId);
				// strData = replaceToken(strData, DriverProfile.KEY_ENGID, car.strEngId);
				// strData = replaceToken(strData, DriverProfile.KEY_BTMID, car.strBtmId);
				strData = "sohu_yzm=&" + strData;
				strData = strData.replace(car.getCityPy(), URLEncoder.encode(car.getCityPy(), "utf-8"));
				// System.out.println(proxy);
				// System.out.println(strData);

				while (Loop < 10) {
					Loop++;
					String strResp = HttpsUtils.postURLContentsWithCookies(strUrl, strData, vCookies, STRING_REFERER, "UTF-8", proxy);
					System.out.println("vCookies=======" + vCookies);
					System.out.println(" strResp  11=======" + strResp);
					int iLoop = 0;
					while (!StringUtil.isNotEmpty(strResp) && iLoop < 2) {
						iLoop++;
						strResp = HttpsUtils.postURLContentsWithCookies(strUrl, strData, vCookies, STRING_REFERER, "UTF-8", proxy);
						System.out.println("sohu strResp 2=======" + strResp);
					}
					// {"ERRMSG":"很多人在查询哦，请稍后再试","ERRCODE":"5002","STATUS":"1"}
					// {"use_cache":"true","STATUS":"0","WZCX":[{"deductScore":"3","content":"机动车违反禁令标志指示的","datestr":"2015-08-09 08:25:00.0",
					// "area":"吴中路近虹镇路路段","breakrulesCode":"13440","status":"未处理","penalty":"200"}]}
					// {"use_cache":"true","ERRMSG":"发动机号有误","ERRCODE":"5003","STATUS":"1"}
					// {"ERRMSG":"机动车号牌或者车辆识别代号输入错误","ERRCODE":"5004","STATUS":"1"}
					if (StringUtil.isNotEmpty(strResp) && !strResp.contains("很多人在查询哦，请稍后再试") && !strResp.contains("\"ERRCODE\":")) {
						if (strResp.contains("\"STATUS\":\"6\"")) {
							strResp = "";
							LogUtil.doMkLogData_sohu(car, "err", "访问次数超限");
							Thread.sleep(1000);
							continue;
						}
						System.out.println("sohu result=======" + strResp);
						if (strResp.contains("ERRCODE\":\"5003")) {
							LogUtil.doMkLogData_sohu(car, "err", "信息输入错误");
							return ResultCache.toErrJsonResult("车辆信息错误,发动机号错误！");
						}
						if (strResp.contains("ERRCODE\":\"5004")) {
							LogUtil.doMkLogData_sohu(car, "err", "信息输入错误");
							return ResultCache.toErrJsonResult("车辆信息错误,车架号错误！");
						}
						LogUtil.doMkLogData_sohu(car, "ok", "");
						ret = parser.parse(strResp, car);

						break;
					} else {
						Thread.sleep(1000);
						strResp = "";

					}
				}
			}
		} catch (SocketTimeoutException e) {

			LogUtil.doMkLogData_sohu(car, "err", "网络连接超时");

			e.printStackTrace();
		} catch (FileNotFoundException e) {

			LogUtil.doMkLogData_sohu(car, "err", "网络连接异常");
			e.printStackTrace();
		} catch (IOException e) {

			LogUtil.doMkLogData_sohu(car, "err", "网络连接异常");
			e.printStackTrace();
		} catch (Exception e) {

			LogUtil.doMkLogData_sohu(car, "err", "其他错误");
			e.printStackTrace();
		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_sohu(car, "err", "其他错误");
		}
		return ret;
	}

	@SuppressWarnings("unused")
	private static String replaceToken(String BUY_XML, String strKey, String strValue) {
		String strRet = BUY_XML;

		try {
			if (StringUtil.isNotEmpty(strRet) && StringUtil.isNotEmpty(strKey)) {
				String strKeyToken = "%" + strKey + "%";
				strRet = strRet.replaceAll(strKeyToken, strValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;
	}

}
