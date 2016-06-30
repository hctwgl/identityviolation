package com.mapbar.traffic.score.transfer.citys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.AnshanParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityAnshanService implements Transfer {

	private static final String GET_VIOLATION_URL = "http://www.asgajj.com/chaxun_2.asp";
	private static final String STRING_REFERER = "http://www.asgajj.com/ok.asp?find=2";
	AnshanParser parser = new AnshanParser();

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
			List<String> cookies = new ArrayList<String>();
			int Loop = 0;
			while (Loop < 3) {
				Loop++;
				try {
					// tb_jszh=210303199004192018&dabb=210103247337
					Map<String, String> params = new HashMap<String, String>();
					params.put("tb_jszh", driverProfile.getDriverLicense());
					params.put("dabb", driverProfile.getLssueArchive());
					String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, cookies, STRING_REFERER, "GBK", next);
					System.out.println("strResp====" + strResp);
					if (StringUtil.isNotEmpty(strResp) && strResp.contains("识别码后四位错误")) {
						return ResultCache.toErrJsonResult("车辆信息错误,车架号错误！");
					} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("机动车违法信息查询结果")) {
						ret = parser.parse(strResp, driverProfile);
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
