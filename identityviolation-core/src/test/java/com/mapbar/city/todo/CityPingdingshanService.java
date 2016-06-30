package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.PingdingshanParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityPingdingshanService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.pdsjj.gov.cn/searchInfoController.do?list";

	private static final String STRING_REFERER = "http://www.pdsjj.gov.cn/newsController.do?search&symbol=jdc";
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

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = "";

		String btmId = car.getCityPy();
		if (btmId.length() > 6) {
			btmId = btmId.substring(btmId.length() - 6, btmId.length());
		} else if (btmId.length() < 6) {
			return ResultCache.toErrJsonResult("查询规则变化，请输入车架号后6位，给您带来的不变，请您谅解！");
		}
		Vector<String> vCookies = new Vector<String>();

		int Loop = 0;
		while (Loop < 3) {
			Loop++;

			try {

				String postData = "symbol=jdc&hpzl=" + car.getCityPy() + "&clsbm=" + btmId + "&hphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy();
				// System.out.println("postData==="+postData);

				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println("strResp===="+strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("截止更新日期车辆无违法信息")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("class=\"w_cont\"")) {
					ret = parser.parse(strResp, car);
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				}
				break;

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

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGU_With_Msg(car, "err", "其他错误");
		}
		return ret;
	}
}
