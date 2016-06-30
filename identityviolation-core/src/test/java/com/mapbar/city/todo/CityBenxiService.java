package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.BenxiParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityBenxiService implements Transfer {
	//private static final String VALIDATE_IMAGE_URL = "http://www.bxjj.gov.cn/wfcx/VerifyImg.aspx";
	// http://www.bxjj.gov.cn/wfcx/xt/index.aspx?hphm=%e8%be%bdEC4826&hpzl=02&code=5017
	private static final String GET_VIOLATION_URL = "http://www.bxjj.gov.cn/wfcx/xt/index.aspx?";
	// private static final String STRING_REFERER = "http://www.bxjj.gov.cn/wfcx/index.aspx";

	// private static final String IMAGE_TYPE="jpg";
	BenxiParser parser = new BenxiParser();

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

		int Loop = 0;
		Vector<String> vCookies = new Vector<String>();

		String btmId = car.getCityPy();
		if (btmId.length() > 4) {
			btmId = btmId.substring(btmId.length() - 4, btmId.length());
		} else if (btmId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误，请输入车架号后4位，谢谢！");
		}

		while (Loop < 1) {
			Loop++;

			try {

				String urlData = "hphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&hpzl=" + car.getCityPy() + "&code=" + btmId;
				String strHtml = HttpsUtils.getURLContentsWithCookies(GET_VIOLATION_URL + urlData, vCookies, null, next, "UTF-8");
				// System.out.println(vCookies);
				// System.out.println(strHtml);

				if (StringUtil.isNotEmpty(strHtml) && strHtml.contains("无此车辆信息")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
					break;
				} else if (StringUtil.isNotEmpty(strHtml)) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = parser.parse(strHtml);
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
