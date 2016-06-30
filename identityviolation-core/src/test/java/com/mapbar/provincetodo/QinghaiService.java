package com.mapbar.provincetodo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.QinghaiParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class QinghaiService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://111.12.128.5:9080/GajtQuery/queryWfxw.queryInfo?method=getVehicleVioList";
	private static final String STRING_REFERER = "http://111.12.128.5:9080/GajtQuery/queryinfo/queryVehicleVio_main.jsp";
	QinghaiParser parser = new QinghaiParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, null);
				// strResult = lookupViolation(car, ProxyManager.next(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = "";

		String btmId = car.getCityPy();
		if (!StringUtil.isNotEmpty(btmId)) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号为空!");
		}
		Vector<String> vCookies = new Vector<String>();
		// hpzl=02&fzjg=青A&ohphm=V8975&hphm=青AV8975&clsbdh=ls4aab366ba164798&button=查 询

		int Loop = 0;
		while (Loop < 3) {
			Loop++;
			try {

				String postData = "hpzl=" + car.getCityPy() + "&fzjg=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy().substring(0, 1) + "&ohphm=" + car.getCityPy().substring(0, 1) + "&hphm=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy() + "&clsbdh=" + btmId + "&button=" + URLEncoder.encode("查 询", "GBK");

				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "GBK", next);

				// System.out.println(strResp);

				if (StringUtil.isNotEmpty(strResp) && strResp.contains("您输入的车辆信息有误，请检查输入")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				} else if (StringUtil.isNotEmpty(strResp)) {
					ret = parser.parse(strResp, car);
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					break;
				}

			} catch (SocketTimeoutException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接超时");
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "其他错误");
				e.printStackTrace();
			}

		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "其他错误");
		}
		return ret;
	}

}
