package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.LiaoyangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityLiaoyangService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.lygajj.gov.cn/wzcx/wzcx.asp";

	private static final String STRING_REFERER = "http://www.lygajj.gov.cn/";

	LiaoyangParser parser = new LiaoyangParser();

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

	private String lookupViolation(DriverProfile driverProfile, HttpsProxy next) {
		String ret = "";

		Vector<String> vCookies = new Vector<String>();
		String btmId = driverProfile.getCityPy();
		if (btmId.length() > 4) {
			btmId = btmId.substring(btmId.length() - 4, btmId.length());
		} else if (btmId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}
		int Loop = 0;
		while (Loop < 1) {
			try {
				Loop++;

				String postData = "cphm=%C1%C9" + driverProfile.getCityPy() + "&cllx=" + driverProfile.getCityPy() + "&djh=" + btmId;
				// System.out.println(postData);
				String strRep = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "GBK", next);
				// System.out.println(strRep);

				if (StringUtil.isNotEmpty(strRep) && strRep.contains("车牌号码、车辆识别代码核查失败，请重新确认")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
					break;
				} else if (strRep.contains("该车辆无违章信息")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("id=\"table2\"")) {
					ret = parser.parse(strRep, driverProfile);
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					break;
				}

			} catch (SocketTimeoutException e) {

				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接超时");

				e.printStackTrace();
			} catch (FileNotFoundException e) {

				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {

				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {

				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "其他错误");
				e.printStackTrace();
			}
		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "其他错误");
		}
		return ret;
	}
}
