package com.mapbar.provincetodo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.NingxiaParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class NingxiaService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.nxjj.gov.cn/sys/wzblg_init.act";
	private static final String STRING_REFERER = "http://www.nxjj.gov.cn/sys/jdcwzGuest_displayAll.act";
	NingxiaParser parser = new NingxiaParser();

	// http://59.49.18.116:8008/sxwwpt_wai/inquire/illegalAction!carInquire.action?type=1&hpzl=02&csjcKey=140000&vioViolation.hphm=AZ0955&clsbdm=033490&authCode=0391&authCode2=0391
	// http://59.49.18.116:8008/sxwwpt_wai/jsp/inquiresf/main.jsp?actype=vehillegal&hpzl=02&csjcKey=140000&vioViolation.hphm=AZ0955&clsbdm=033490&authCode=5159&authCode2=5159
	// http://59.49.18.116:8008/sxwwpt_wai/randinquire?metod=vehillegal2&d=1420709431758
	// http://59.49.18.116:8008/sxwwpt_wai/jsp/admin/frmCodeAction!queryAuth.action
	// REfer=http://59.49.18.116:8008/sxwwpt_wai/jsp/inquires/querymotorIllegalc.jsp

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

		String btmId = car.getCityPy();
		if (btmId.length() > 6) {
			btmId = btmId.substring(btmId.length() - 6, btmId.length());
		} else if (btmId.length() < 6) {
			return ResultCache.toErrJsonResult("车辆信息错误,请输入车架号后六位！");
		}

		int Loop = 0;
		while (Loop < 3) {
			Loop++;
			try {

				// hpzl:02
				// hphm:宁AFE998
				// clsbdh:333742
				// Response Headersview source

				// hpzl=02&hphm=%E5%AE%81AFE998&clsbdh=333742&Submit=+%E9%87%8D+%E5%A1%AB+&=true
				String postData = "hpzl=" + car.getCityPy() + "&&hphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&clsbdh=" + btmId + "&Submit=+%E9%87%8D+%E5%A1%AB+&=true";
				// System.out.println(postData);
				String strResp = HttpsUtils.postURLContents(GET_VIOLATION_URL, STRING_REFERER, postData, "UTF-8");
				// System.out.println(strResp);
				// "{\"message\":\"您目前没有违法记录，向遵纪守法的交通参与者致敬！\",\"dealFlag\":\"NO\",\"other\":\"\"}"
				// "{\"message\":\"\",\"dealFlag\":\"OK\",\"other\":\"\"}"
				// wzblform resultlist
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("您目前没有违法记录")) {
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("你输入的信息有误")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("dealFlag\\\":\\\"OK")) {
					// System.out.println("in deal");
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					String resp = HttpsUtils.postURLContents(STRING_REFERER, STRING_REFERER, "hpzl=02&hphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&clsbdh=" + btmId, "UTF-8");
					// System.out.println(resp);
					ret = parser.parse(resp, car);
				}

			} catch (SocketTimeoutException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接超时");
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "其他错误");
				e.printStackTrace();
			}

		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanProv(car, "err");
		}
		return ret;
	}
}
