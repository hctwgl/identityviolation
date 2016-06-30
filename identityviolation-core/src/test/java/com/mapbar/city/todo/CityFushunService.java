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
import com.mapbar.traffic.score.parser.citys.FushunParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityFushunService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.fs110.gov.cn/Form/MoreNews.aspx?lx=116";
	// http://www.fs110.gov.cn/Form/MoreNews.aspx?lx=116

	private static final String STRING_REFERER = "http://www.fs110.gov.cn/Form/MoreNews.aspx?lx=116";
	FushunParser parser = new FushunParser();

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

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret = "";

		Vector<String> vCookies = new Vector<String>();

		int Loop = 0;
		while (Loop < 1) {
			try {
				Loop++;

				String indexStr = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
				System.out.println("vCookies========" + vCookies);
				Map<String, String> hiddenMap = parser.parseHidden(indexStr);
				System.out.println(hiddenMap);
				String paramDate = parser.getParamData(hiddenMap);
				System.out.println(paramDate);
				//String postData = paramDate + "&ctl00$ContentPlaceHolder1$MoreList1$DrvVio1$Drop_CarPai=" + URLEncoder.encode(car.getCityPy() + car.getCityPy().substring(0, 1), "UTF-8") + "&ctl00$ContentPlaceHolder1$MoreList1$DrvVio1$Text_CarPai=" + URLEncoder.encode(car.getCityPy().substring(1), "UTF-8") + "&ctl00$ContentPlaceHolder1$MoreList1$DrvVio1$Drop_CarLx=" + URLEncoder.encode("小型汽车", "UTF-8")
				//		+ "&ctl00$ContentPlaceHolder1$MoreList1$DrvVio1$But_CX=" + "&DXScript=" + URLEncoder.encode("1_32,2_22,2_29,1_61,2_15,1_39,1_52,3_7,2_21,1_36,1_54,1_51,2_16", "UTF-8");
				// System.out.println("postData======="+postData);
				String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "UTF-8", next);

				// System.out.println("strRep======="+strRep);
				if (strRep.contains("没有数据")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("id=\"ctl00_ContentPlaceHolder1_MoreList1_DrvVio1_GridView_DXMainTable")) {
					ret = parser.parse(strRep);
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
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

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGU_With_Msg(car, "err", "其他错误");
		}
		return ret;
	}

}
