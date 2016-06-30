package com.mapbar.traffic.score.transfer.citys;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.LuoyangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityLuoyangService implements Transfer {

	// private static final String VALIDATE_IMAGE_URL = "http://bespeak.zzjtgl.cn/zxywbl/GetYzmCode.aspx?t=";
	private static final String GET_VIOLATION_URL = "http://www.lyjjzd.com/GetDriving.php?";
	private static final String STRING_REFERER = "http://www.lyjjzd.com/";

	LuoyangParser parser = new LuoyangParser();

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

		List<String> vCookies = new ArrayList<String>();

		int Loop = 0;
		while (Loop < 3) {
			try {
				Loop++;
				String validataCode = "ABCD";
				String[] selectChar = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
				// 所有候选组成验证码的字符，当然也可以用中文的
				for (int i = 0; i < 4; i++) {
					int charIndex = (int) Math.floor(Math.random() * 36);
					validataCode += selectChar[charIndex];
				}
				// Tbx_DirverL=410326199203093751&Tbx_DirverD=410300961228&input1=y0yu&Search1=%B2%E9%D1%AF
				Map<String, String> params = new HashMap<String, String>();
				params.put("Tbx_DirverL", driverProfile.getDriverLicense());
				params.put("Tbx_DirverD", driverProfile.getLssueArchive());
				params.put("input1", validataCode);
				params.put("Search1", URLEncoder.encode("查询", "GBK"));
				String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, vCookies, STRING_REFERER, "GBK", next);
				// String strRep1 = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, "Tbx_DirverL=410326199203093751&Tbx_DirverD=410300961228&input1=y0yu&Search1=%B2%E9%D1%AF", vCookies, STRING_REFERER, "GBK", ProxyManager.nextHttpsProxy(true));
				//System.out.println(strRep);
				// System.out.println(strRep1);
				// System.out.println(strRep.length() + "   " + strRep1.length());

				if (StringUtil.isNotEmpty(strRep) && !strRep.contains("基本信息")) {
					continue;
				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("没有找到驾驶员信息")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号或档案编号错误！");
					break;
				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("驾驶员违法信息")) {
					ret = parser.parse(strRep, driverProfile);
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
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
			LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "其他错误");
		}
		return ret;
	}

}
