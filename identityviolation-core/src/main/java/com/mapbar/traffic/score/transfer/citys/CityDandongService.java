package com.mapbar.traffic.score.transfer.citys;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.DandongParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityDandongService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.dd122.com/pages/chaxun02.asp";

	private static final String STRING_REFERER = "http://www.dd122.com/pages/xxcx.asp";

	DandongParser parser = new DandongParser();

	@Override
	public String checkDriverScore(DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (driverProfile != null && StringUtil.isNotEmpty(driverProfile.getCityPy())) {
				strResult = lookupScore(driverProfile, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	private String lookupScore(DriverProfile driverProfile, HttpHost next) {
		String ret = "";

		List<String> cookies = new ArrayList<String>();
		int Loop = 0;
		while (Loop < 1) {
			try {
				Loop++;
				// Map<String, String> params = new LinkedHashMap<String, String>();
				// params.put("xm", URLEncoder.encode(driverProfile.getDriverName(), "GBK"));
				// params.put("dabh", driverProfile.getLssueArchive());
				// params.put("Submit222", URLEncoder.encode("查询", "GBK"));
				// String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, cookies, STRING_REFERER, "GBK", next);
				String params = "xm=" + URLEncoder.encode(driverProfile.getDriverName(), "GBK") + "&dabh=" + driverProfile.getLssueArchive() + "&Submit222=" + URLEncoder.encode("查询", "GBK");
				String strRep = HttpClientUtil.postURLContentsWithCookiesString(GET_VIOLATION_URL, params, cookies, STRING_REFERER, "GBK", next);
				// System.out.println(strRep + " " + strRep.length());
				if (StringUtil.isNotEmpty(strRep) && strRep.contains("请输入正确的驾驶员姓名和档案编号")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("驾驶证信息错误,档案编号或姓名错误！");
					break;
				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("驾驶员基本信息")) {
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
