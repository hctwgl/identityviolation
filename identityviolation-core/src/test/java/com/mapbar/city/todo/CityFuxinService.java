package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.FuxinParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityFuxinService implements Transfer {
	// http://218.24.200.244/
	private static final String GET_VIOLATION_URL = "http://218.24.200.244/s1/?action=t1";

	private static final String STRING_REFERER = "http://218.24.200.244/";

	FuxinParser parser = new FuxinParser();

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

		Vector<String> vCookies = new Vector<String>();
		String engId = car.getCityPy();
		if (engId == null || "".equals(engId) || "选填".equals(engId)) {
			return ResultCache.toErrJsonResult("车辆信息错误,发送机号不能为空！");
		}
		int Loop = 0;
		while (Loop < 1) {
			try {
				Loop++;

				String urlData = "&clsbdh=" + engId + "&zl=" + car.getCityPy() + "&hm=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy();

				String postData = "clsbdh=" + engId + "&hpzl=" + car.getCityPy() + "&hp1=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy().substring(0, 1) + "&hp2=" + car.getCityPy().substring(1) + "&sbt=%B2%E9%D1%AF";

				String strRep = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL + urlData, postData, vCookies, STRING_REFERER, "GBK", next);

				System.out.println(strRep);
				if (StringUtil.isNotEmpty(strRep) && strRep.contains("发动机号输入错误,请您重新输入进行查询")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("车辆信息错误,发动机号或车牌号错误！");
					break;
				} else if (strRep.contains("没有相关的违法信息")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("id=\"tbl\"")) {
					ret = parser.parse(strRep);
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
