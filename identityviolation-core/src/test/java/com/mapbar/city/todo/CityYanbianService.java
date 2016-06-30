package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.YanbianParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityYanbianService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.ybjg.gov.cn/queryWzch.jspx";

	// hphm=%E5%90%89HHD949&FDJH=3071&hpzl=02&imageField2.x=57&imageField2.y=19
	// Referer: http://www.ybjg.gov.cn/
	private static final String STRING_REFERER = "http://www.ybjg.gov.cn/";
	YanbianParser parser = new YanbianParser();

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
		if (btmId.length() > 4) {
			btmId = btmId.substring(btmId.length() - 4, btmId.length());
		} else if (btmId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}
		Vector<String> vCookies = new Vector<String>();

		int Loop = 0;
		while (Loop < 3) {
			Loop++;

			try {
				// http://www.kfpolice.com/web/wsbs/OnRedirect.aspx?
				// type=vehicle&hphm=%E8%B1%ABBYJ393&fdjh=18E20410065&hpzl=02&sbdm=LZWADAGA7EB426600

				// System.out.println("vCookies==="+vCookies);
				// String urlData="hphm="+URLEncoder.encode(car.strState, "UTF-8")+car.strRegId+"&fdjh="+car.strEngId+"&hpzl=02&sbdm="+car.strBtmId;
				String postData = "hphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&FDJH=" + btmId + "&hpzl=" + car.getCityPy() + "&imageField2.x=57&imageField2.y=19";
				// System.out.println("postData==="+postData);
				// hphm=%E5%90%89HHD949&FDJH=3071&hpzl=02&imageField2.x=57&imageField2.y=19
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println("strResp===="+strResp);

				if (StringUtil.isNotEmpty(strResp) && strResp.contains("\"zt\":\"正常\"")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("var json= {\"success\":false}")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("违法未处理")) {
					// ret = parser.parse(strResp);
					Pattern p = Pattern.compile("var\\sjson=\\s(.*?)}]};");
					Matcher match = p.matcher(strResp);
					String json = "";
					if (match.find()) {
						json = match.group(1);
					}
					json = json + "}]}";

					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = parser.parse(json, car);
					break;
				}
				break;
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
			LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "其他错误");
		}
		return ret;
	}
}
