package com.mapbar.city.todo;

import java.io.IOException;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.FushunParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityFushunService2 implements Transfer {
	// http://www.fsjjzd.gov.cn/cxjg.asp?kind=weifa

	private static final String GET_VIOLATION_URL = "http://www.fsjjzd.gov.cn/cxjg.asp?kind=weifa";
	// http://www.fs110.gov.cn/Form/MoreNews.aspx?lx=116

	private static final String STRING_REFERER = "http://www.fsjjzd.gov.cn/wfchaxun.htm";
	FushunParser parser = new FushunParser();

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
		while (Loop < 1) {
			try {
				Loop++;

				String postData = "sf=%C1%C9&carno=" + car.getCityPy() + "&type1=%D0%A1%D0%CD%C6%FB%B3%B5";

				String strRep = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "GBK", next);

				if (strRep.contains("无此数据！")) {
					LogUtil.doMkLogData_jiaoguanju(car, "ok");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("id=ess_ctr1797_ModuleContent")) {
					ret = parser.parse(strRep);
					LogUtil.doMkLogData_jiaoguanju(car, "ok");
					break;
				}

			} catch (IOException e) {
				LogUtil.doMkLogData_jiaoguanju(car, "err");
				System.out.print("IOException");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_jiaoguanju(car, "err");
				e.printStackTrace();
			}
		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return ret;
	}
}
