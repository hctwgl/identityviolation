package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.PuyangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityPuyangService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://125.46.53.194:88/Lyzjj/hphm.aspx";

	private static final String STRING_REFERER = "http://125.46.53.194:88/Lyzjj/hphm.aspx";
	PuyangParser parser = new PuyangParser();

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

		Vector<String> vCookies = new Vector<String>();

		int Loop = 0;
		while (Loop < 4) {
			try {
				Loop++;

			 HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
				//Map<String, String> hiddenMap = parser.parseHidden(indexStr);
				// System.out.println(hiddenMap);
				// TextBox1:豫JT5258
				// DropDownList1:02
				// Button1:查询
				//String postData = parser.getParamData(hiddenMap) + "&DropDownList1=" + car.getCityPy() + "&TextBox1=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&Button1=" + URLEncoder.encode("查询", "UTF-8");
				String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "UTF-8", next);

				if (StringUtil.isNotEmpty(strRep) && strRep.contains("无违章信息！")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");

					break;
				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("id=\"GridView1\"")) {
					ret = parser.parse(strRep, car);
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
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return ret;
	}
}
