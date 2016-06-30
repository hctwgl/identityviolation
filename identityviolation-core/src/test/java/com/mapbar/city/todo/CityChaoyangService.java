package com.mapbar.city.todo;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.ChaoyangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityChaoyangService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.zgcy.gov.cn/jtwzcx/JTWZ.aspx";

	private static final String STRING_REFERER = "http://www.zgcy.gov.cn/jtwzcx/JTWZ.aspx";

	private static final String ROOT_URL = "http://zgcy.gov.cn/";

	ChaoyangParser parser = new ChaoyangParser();

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
		try {

			int Loop = 0;
			while (Loop < 4) {
				Loop++;

				try {
					Vector<String> vCookies = new Vector<String>();
					HttpsUtils.getURLContentsWithCookies(ROOT_URL, vCookies, ROOT_URL, next);
					// System.out.println(vCookies);
					String indexHtml = HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
					// System.out.println("indexHtml=="+indexHtml);
					// System.out.println(vCookies);
					Map<String, String> hidden = parser.parseHidden(indexHtml);
					// ctl00$ContentPlaceHolder1$txtWhere:
					// ctl00$ContentPlaceHolder1$txtNO:辽N20X63
					// ctl00$ContentPlaceHolder1$ddl:0
					// ctl00$ContentPlaceHolder1$hfID:2894
					// ctl00$ContentPlaceHolder1$Button1:查询

					String postData = parser.getParamData(hidden) + "&ctl00$ContentPlaceHolder1$txtWhere=&ctl00$ContentPlaceHolder1$txtNO=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&ctl00$ContentPlaceHolder1$ddl=0&" + "ctl00$ContentPlaceHolder1$Button1=" + URLEncoder.encode("查询", "UTF-8");
					// System.out.println(postData);
					String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
					// System.out.println("strResp=="+strResp);

					if (strResp.contains("未找到违章记录!!")) {
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					} else if (strResp.contains("table class=\"gridtable\"")) {
						ret = parser.parse(strResp);
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						break;
					}

				} catch (Exception e) {
					LogUtil.doMkLogData_jiaoguanju(car, "err");
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// LogHelp.doMkLogData_jiaoguanju(car, "err");
			e.printStackTrace();
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return ret;
	}
}
