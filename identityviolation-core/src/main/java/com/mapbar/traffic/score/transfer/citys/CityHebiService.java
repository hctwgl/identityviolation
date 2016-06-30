package com.mapbar.traffic.score.transfer.citys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.HebiParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityHebiService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.hebijj.gov.cn/jfcx.jsp";
	private static final String STRING_REFERER = "http://www.hebijj.gov.cn/cxyw.jsp?fid=3";

	HebiParser parser = new HebiParser();

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
		try {
			List<String> vCookies = new ArrayList<String>();
			int Loop = 0;
			while (Loop < 1) {
				try {
					Loop++;
					HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "GBK");
					// System.out.println(vCookies);
					String validataCode = "ABCD";
					String[] selectChar = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
					// 所有候选组成验证码的字符，当然也可以用中文的

					for (int i = 0; i < 4; i++) {
						int charIndex = (int) Math.floor(Math.random() * 36);
						validataCode += selectChar[charIndex];
					}
					// String postDate = "cph=" + URLEncoder.encode(driverProfile.getCityPy(), "GBK") + driverProfile.getCityPy() + "&clx=02&sbm=" + btmId + "&yzm=" + validataCode + "&image2.x=28&image2.y=12";
					// String postDate = "jszh=" + driverProfile.getDriverLicense() + "&dabh=" + driverProfile.getLssueArchive() + "&yzm=" + validataCode;
					Map<String, String> params = new HashMap<String, String>();
					params.put("jszh", driverProfile.getDriverLicense());
					params.put("dabh", driverProfile.getLssueArchive());
					params.put("yzm", validataCode);
					String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, vCookies, STRING_REFERER, "GBK", next);
					//System.out.println(strRep);

					if (StringUtil.isNotEmpty(strRep) && strRep.contains("alert('此信息不存在！')")) {
						ret = ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号或档案编号错误！");
						break;
					} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("onMouseMove") && strRep.contains("onMouseOut")) {
						ret = parser.parse(strRep,driverProfile);
						LogUtil.doMkLogData_jiaoguanju(driverProfile, "ok");
						break;
					}

				} catch (Exception e) {
					LogUtil.doMkLogData_jiaoguanju(driverProfile, "err");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanju(driverProfile, "err");
		}
		return ret;
	}

}
