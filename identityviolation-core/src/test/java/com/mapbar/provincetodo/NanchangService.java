package com.mapbar.provincetodo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class NanchangService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://jx.jxhld.gov.cn:20002/info/carWf.do";
	//private static final String STRING_REFERER1 = "http://www.ncga.gov.cn:81/MainPages/SearchCenter/VehiclesIllegal";
	// http://www.ncga.gov.cn:81/MainPages/SearchCenter/car_wf_result?date=
	private static final String STRING_REFERER2 = "http://www.ncga.gov.cn:81/MainPages/SearchCenter/car_wf_result?date=";

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
			Vector<String> vCookies = new Vector<String>();

			String engId = car.getCityPy();
			if (engId.length() > 6) {
				engId = engId.substring(engId.length() - 6, engId.length());
			} else if (engId.length() < 6) {
				return ResultCache.toErrJsonResult("车辆信息错误,请输入发动机号后六位！");
			}

			int Loop = 0;
			while (Loop < 1) {
				Loop++;
				try {
					// 该车辆暂无违法信息
					// 您输入车辆发动机号后六位不正确
					// 您输入车牌号码或机动车类型不正确
					@SuppressWarnings("deprecation")
					String refer = STRING_REFERER2 + new Date().toGMTString();

					// String refStr = NetHelper.getURLContentsWithCookies(refer, vCookies, STRING_REFERER1, next, "UTF-8");
					// System.out.println(refStr);
					// System.out.println(vCookies);
					// String postDate = "hpno="+URLEncoder.encode(car.getCityPy(), "UTF-8")+"&hphm="+car.getCityPy()+"&hpzl=02&clsbdh="+engId;
					Map<String, String> postDate = new HashMap<String, String>();
					String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, refer, "UTF-8", next);
					System.out.println(strResp);

					if (StringUtil.isNotEmpty(strResp) && strResp.contains("该车辆暂无违法信息")) {
						// LogHelp.doMkLogData_jiaoguanProv(car, "ok");
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("您输入车辆发动机号后六位不正确")) {
						LogUtil.doMkLogData_jiaoguanProv(car, "ok");
						ret = ResultCache.toErrJsonResult("车辆信息错误,发动机号错误！");
					} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("您输入车牌号码或机动车类型不正确")) {
						LogUtil.doMkLogData_jiaoguanProv(car, "ok");
						ret = ResultCache.toErrJsonResult("车辆信息错误,车牌号或机动车类型不正确！");
					} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("class=\"Mytable\"")) {
						System.out.println("in deal");

					}
				} catch (Exception e) {
					// LogHelp.doMkLogData_jiaoguanProv(car, "err");
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			// LogHelp.doMkLogData_jiaoguanju(car, "err");
			e.printStackTrace();
		}
		// if("".equals(ret)){
		// LogHelp.doMkLogData_jiaoguanProv(car, "err");
		// }
		return ret;
	}

}
