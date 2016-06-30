package com.mapbar.traffic.score.transfer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.WxGuangdongParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class WxGuangdongService implements Transfer {

	// http://weixinfsems.u-road.com/fsyz/license/licenseQuery/441481198510032256/441400883093/ 驾驶证号和驾驶证编号
	private static final String GET_VIOLATION_URL = "http://weixinfsems.u-road.com/fsyz/license/licenseQuery/";
	// private static final String STRING_REFERER = "";

	WxGuangdongParser parser = new WxGuangdongParser();

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

	private String lookupScore(DriverProfile driverProfile, HttpHost next) throws TimeoutException, IOException {
		String ret = "";

		int Loop = 0;

		while (Loop < 3) {
			Loop++;
			try {
				String strResp = HttpClientUtil.getURLContents(GET_VIOLATION_URL + driverProfile.getDriverLicense() + "/" + driverProfile.getLssueArchive(), next);
				// System.out.println("strResp======" + strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("错误信息")) {
					LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号或驾驶证编号有误！");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("查询到以下驾驶证信息")) {
					ret = parser.parse(strResp, driverProfile);
					LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "ok", "");
					break;
				}
			} catch (Exception e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "其他错误");
				e.printStackTrace();
			}
		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanProv(driverProfile, "err");
		}
		return ret;
	}

}
