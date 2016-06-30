package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.JiaozuoParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityJiaozuoService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.jzga.gov.cn/wzcx/lookup.aspx";

	private static final String STRING_REFERER = "http://www.jzga.gov.cn/wzcx/lookup.aspx";

	JiaozuoParser parser = new JiaozuoParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = "";

		String btmId = car.getCityPy();
		if (btmId.length() > 6) {
			btmId = btmId.substring(btmId.length() - 6, btmId.length());
		} else if (btmId.length() < 6) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}
		Vector<String> vCookies = new Vector<String>();

		int Loop = 0;
		while (Loop < 3) {
			Loop++;

			try {

				String indexStr = HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
				// System.out.println("indexStr==="+indexStr);
				Map<String, String> hidden = parser.parseHidden(indexStr);
				// System.out.println("vCookies==="+vCookies);
				//
				String postData = parser.getParamData(hidden) + "&TBHM=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy() + "&DJH=" + btmId + "&BSearch=%B2%E9%D1%AF";

				// System.out.println("postData==="+postData);

				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "GBK", next);
				// System.out.println("strResp===="+strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("识别码后四位错误")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("以下信息为交通民警现场处理违法信息")) {
					ret = parser.parse(strResp, car);
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				}

			} catch (SocketTimeoutException e) {

				LogUtil.doMkLogData_JGU_With_Msg(car, "err", "网络连接超时");

				e.printStackTrace();
			} catch (FileNotFoundException e) {

				LogUtil.doMkLogData_JGU_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {

				LogUtil.doMkLogData_JGU_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {

				LogUtil.doMkLogData_JGU_With_Msg(car, "err", "其他错误");
				e.printStackTrace();
			}
		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGU_With_Msg(car, "err", "其他错误");
		}
		return ret;
	}

}
