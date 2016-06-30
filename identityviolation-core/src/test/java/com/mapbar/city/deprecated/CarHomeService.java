package com.mapbar.city.deprecated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class CarHomeService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.autohome.com.cn/Violation/Violation/GetViolationList";
	private static final String STRING_REFERER = "http://www.autohome.com.cn/violation/violationlist";

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, ProxyManager.next(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret = "";
		try {
			List<String> vCookies = new ArrayList<String>();

			// String postDate0 = "cityid=110100&platenum=" + URLEncoder.encode(car.getPro());

			// String strResp0 = NetHelper.postURLContentsWithCookiesF("http://www.autohome.com.cn/violation/violationlist",
			// postDate0, vCookies, STRING_REFERER, next);
			// System.out.println(vCookies);
			// System.out.println("strResp0====="+strResp0);
			// String postDate = "cityid=110100&platenum=" + URLEncoder.encode(car.getPro());
			Map<String, String> postData = new HashMap<String, String>();
			String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, next);
			System.out.println(vCookies);
			System.out.println("strResp==========" + strResp);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return ret;
	}

}
