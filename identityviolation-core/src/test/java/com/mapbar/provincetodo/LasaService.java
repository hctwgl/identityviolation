package com.mapbar.provincetodo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.PingdingshanParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class LasaService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://ga.lasa.gov.cn/CarQuery/query/queryJson";

	private static final String STRING_REFERER = "http://ga.lasa.gov.cn/Mobile/CarQuery/Index";
	// hpzl=02&haoma=%C1%C9C&haoma1=X0043&xnh=4984&button=%B2%E9%D1%AF
	PingdingshanParser parser = new PingdingshanParser();

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

	//

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = "";

		Vector<String> vCookies = new Vector<String>();

		int Loop = 0;
		while (Loop < 3) {
			Loop++;

			try {

				String postData = "area=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + "&plate=" + car.getCityPy();
				// System.out.println("postData==="+postData);
				// NetUtil.getURLContentsWithCookiesEx(STRING_REFERER, vCookies, STRING_REFERER, next);
				// System.out.println(vCookies);
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println("strResp===="+strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.equals("[]")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				}
				if (StringUtil.isNotEmpty(strResp)) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = parser(strResp);
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
		return ret;
	}

	private String parser(String strResp) {
		String strResult = null;
		try {
			JSONArray jcases = JSONArray.parseArray(strResp);
			if (jcases.size() > 0) {
				DriverCase vc = new DriverCase();
				JSONArray ret = new JSONArray();
				JSONObject jcase = null;
				for (int i = 0; i < jcases.size(); i++) {
					JSONObject obj = (JSONObject) jcases.get(i);
					jcase = new JSONObject();
					jcase.put("序号", String.valueOf(i + 1));
					jcase.put("处理情况", "未处理");
					jcase.put("记分", "未知");
					jcase.put("罚款", "未知");
					jcase.put("违法时间", (obj.containsKey("behaviortime") ? obj.get("behaviortime") : "未知") + ":00");
					jcase.put("违法地点", (obj.containsKey("cam_location") ? obj.get("cam_location") : "未知") + " " + (obj.containsKey("direction") ? obj.get("direction") : ""));
					jcase.put("违法内容", obj.containsKey("behavior") ? obj.get("behavior") : "未知");
					if (jcase != null) {
						ret.add(jcase);
					}
				}
				if (jcases.size() > 0) {
					vc.json.put("data", ret);

					vc.json.put("count", ret.size());

					vc.json.put("status", "ok");

					strResult = vc.toString();
				} else {
					strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strResult;
	}
}
