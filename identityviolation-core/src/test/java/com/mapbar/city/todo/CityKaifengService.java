package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.KaifengParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityKaifengService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.kfpolice.com/web/wsbs/VehicleQuery.aspx?";

	// hpzl=02&haoma=%C1%C9C&haoma1=X0043&xnh=4984&button=%B2%E9%D1%AF
	KaifengParser parser = new KaifengParser();

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

		int Loop = 0;
		while (Loop < 3) {
			Loop++;

			try {
				// http://www.kfpolice.com/web/wsbs/OnRedirect.aspx?
				// type=vehicle&hphm=%E8%B1%ABBYJ393&fdjh=18E20410065&hpzl=02&sbdm=LZWADAGA7EB426600

				// System.out.println("vCookies==="+vCookies);
				String urlData = "hphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&fdjh=" + car.getCityPy() + "&hpzl=" + car.getCityPy() + "&sbdm=" + car.getCityPy();

				// System.out.println("postData==="+postData);

				String strResp = HttpsUtils.getURLContentsWithCookies(GET_VIOLATION_URL + urlData, vCookies, null, next, "UTF-8");
				// System.out.println("strResp===="+strResp);

				if (StringUtil.isNotEmpty(strResp) && strResp.contains("<span id=\"lblZT\">正常</span>")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("<span id=\"lblHphm\"></span>")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,发动机号或车架号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("违法未处理</font></span>")) {
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
		// 开封 kaifeng 豫BYJ393 车架号：LZWADAGA7EB426600 发动机号：18E20410065
		// kaifeng 豫BBY906 车架号：L6T7824S7EN138595 发动机号：E6C900590
		// http://www.kfpolice.com/web/wsbs/VehicleQuery.aspx?hphm=%E8%B1%ABBBY906&fdjh=E6C900590&hpzl=02&sbdm=L6T7824S7EN138595

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGU_With_Msg(car, "err", "其他错误");
		}
		return ret;
	}
}
