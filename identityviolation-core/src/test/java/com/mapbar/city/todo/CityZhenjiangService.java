package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.ZhenjiangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityZhenjiangService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.zjcgs.gov.cn/wfcx.aspx";

	private static final String STRING_REFERER = "http://www.zjcgs.gov.cn/wfcx.aspx";

	ZhenjiangParser parser = new ZhenjiangParser();

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

	private String lookupViolation(DriverProfile driverProfile, HttpHost next) {
		String ret = "";

		String btmId = driverProfile.getCityPy();
		if (btmId.length() > 4) {
			btmId = btmId.substring(btmId.length() - 4, btmId.length());
		} else if (btmId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}

		int Loop = 0;
		while (Loop < 3) {
			Loop++;

			try {
				Vector<String> vCookies = new Vector<String>();
				String indexStr = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
				Map<String, String> hidden = parser.parseHidden(indexStr);
				// System.out.println("indexStr==="+indexStr);
				// System.out.println("vCookies==="+vCookies);

				// Ddl_hpzl:02:小型汽车
				// T_hphm:HZ660
				// T_clsbdh:2389
				// B_save:提交信息
				// Ddl_hpzl0:02:小型汽车
				// T_hphm0:
				// T_clsbdh0:
				// Ddl_hpzl1:02:小型汽车
				// T_hphm1:
				// T_clsbdh1:
				// T_sfzh:
				// T_dabh:
				// T_sfzh0:
				// T_dabh0:
				hidden.put("__EVENTTARGET", "T_clsbdh");
				//String postData = parser.getParamData(hidden) + "&Ddl_hpzl=" + URLEncoder.encode("02:小型汽车", "UTF-8") + "&T_hphm=" + driverProfile.getCityPy().substring(1) + "&T_clsbdh=" + btmId + "&B_save=" + URLEncoder.encode("提交信息", "UTF-8") + "&Ddl_hpzl0=" + URLEncoder.encode("02:小型汽车", "UTF-8") + "&T_hphm0=&T_clsbdh0=&Ddl_hpzl1=" + URLEncoder.encode("02:小型汽车", "UTF-8") + "&T_hphm1=&T_clsbdh1=&T_sfzh=&T_dabh=&T_sfzh0=&T_dabh0=";
				// System.out.println(postData);
				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "UTF-8", next);
				System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("车架号后四位有误")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				} else if (strResp.contains("共0条违章") || strResp.contains("该车未发现违法行为")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					break;
				} else if (strResp.contains("class=\"GridViewStyle\"")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					ret = parser.parse(strResp, driverProfile);
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
			LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "验证码识别失败");
		}
		return ret;
	}
}
