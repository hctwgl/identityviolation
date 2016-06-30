package com.mapbar.traffic.score.transfer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.HebeiParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class HebeiService implements Transfer {

	private static final String GET_VALIDATA_URL = "http://www.hbgajg.com/service/show-13-43.html";
	private static final String STRING_REFERER = "http://www.hbgajg.com/";

	HebeiParser parser = new HebeiParser();

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

		int Loop = 0;
		while (Loop < 3) {
			Loop++;
			try {
				// String strData="CC_JSR_Two="+driverProfile.getDriverLicense()+"&CC_JSR_One="+driverProfile.getLssueArchive();
				Map<String, String> params = new HashMap<String, String>();
				params.put("CC_JSR_Two", driverProfile.getDriverLicense());
				params.put("CC_JSR_One", driverProfile.getLssueArchive());
				String strResp = HttpClientUtil.postURLContentsToCheckForm(GET_VALIDATA_URL, params, STRING_REFERER, "gbk", next);
				// System.out.println("strResp========"+strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("无相关记录！请检查您的档案编号/身份证号码")) {
					LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号或档案编号号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("驾驶人基本信息查询结果")) {
					JSONObject jobj = new JSONObject();
					parser.parse(strResp, jobj, driverProfile);
					DriverCase vc = new DriverCase();
					if (jobj.size() > 0) {
						vc.json.put("data", jobj);
						vc.json.put("status", "ok");
						ret = vc.toString();
					} else {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}
					LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "ok", "");
					break;
				}
			} catch (IOException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "其他错误");
				e.printStackTrace();
			}
		}
		return ret;
	}

}
