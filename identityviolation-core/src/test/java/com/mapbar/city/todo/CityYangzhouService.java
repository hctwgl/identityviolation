package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.YangzhouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityYangzhouService implements Transfer {

	private static final String GET_VIOLATION_URL = "http://218.106.97.174:8080/cx/queryVehInfoGM.do";
	private static final String STRING_REFERER1 = "http://218.106.97.174:8080/cx/";

	private static final String STRING_REFERER = "http://218.106.97.174:8080/cx/module/outside/viewVehicleInfo.jsp";

	private static final String ROOT_URL = "http://www.yzjxw.com/";
	//
	YangzhouParser parser = new YangzhouParser();

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

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret = "";

		String btmId = car.getCityPy();
		if (btmId.length() > 6) {
			btmId = btmId.substring(btmId.length() - 6, btmId.length());
		} else if (btmId.length() < 6) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}

		int Loop = 0;
		while (Loop < 2) {
			Loop++;

			try {
				Vector<String> vCookies = new Vector<String>();
				HttpClientUtil.getURLContentsWithCookies(ROOT_URL, vCookies, ROOT_URL, next);

				// System.out.println("vCookies==="+vCookies);
				 HttpClientUtil.getURLContentsWithCookies(STRING_REFERER1, vCookies, ROOT_URL, next);

				HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER1, next);
				System.out.println("vCookies===" + vCookies);
				String postData = "method=selectVehInfo&hpzl=" + car.getCityPy() + "&cphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&clsbdh=" + btmId + "&startTime=2014-12-15&endTime=2015-12-15";
				System.out.println(postData);
				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "UTF-8", next);
				System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("您所输入的验证码与车辆信息不符，请检查修改")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				} else if (strResp.contains("暂未查询到您车辆的交通违法记录")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				} else if (strResp.contains("查询结果：") && strResp.contains("<div id=\"mainT\">")) {
					ret = parser.parse(strResp);
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
