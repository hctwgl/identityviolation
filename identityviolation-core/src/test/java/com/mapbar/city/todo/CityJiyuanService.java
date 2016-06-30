package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.JiyuanImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.JiyuanParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityJiyuanService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.jycgs.com/wfcx/GetYzmCode.ashx";

	private static final String GET_VIOLATION_URL = "http://www.jycgs.com/wfcx/jdc_cx.aspx";

	private static final String STRING_REFERER = "http://www.jycgs.com/wfcx/jdc_cx.aspx";
	private static final String IMAGE_TYPE = "jpg";
	JiyuanParser parser = new JiyuanParser();

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
		String btmId = car.getCityPy();
		if (btmId == null || "".equals(btmId) || "选填".equals(btmId)) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号不能为空");
		}

		@SuppressWarnings("unused")
		Map<String, String> hiddenMap = null;
		int Loop = 0;
		while (Loop < 12) {

			Loop++;
			try {

				if (vCookies.isEmpty()) {
					String indexStr = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
					// System.out.println(vCookies);
					// System.out.println(indexStr);
					hiddenMap = parser.parseHidden(indexStr);
				}

				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				// System.out.println(vCookies);
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}
				JiyuanImageFilter imageFilter = new JiyuanImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String valCode = "";
				try {
					valCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					// System.out.println("  valCode=========" + valCode);
					valCode = valCode.replace("fl", "8");
					valCode = valCode.replace("ﬂ", "8");
					valCode = valCode.replace("n", "0");
					valCode = valCode.replace("u", "0");
					valCode = valCode.replace("E", "6");
					valCode = valCode.replace("a", "8");
					valCode = valCode.replace(":", "7");
					valCode = valCode.replace("B", "9");
					valCode = valCode.replace("s", "6");
					valCode = valCode.replaceAll("[^0-9a-zA-Z]", "");
					// System.out.println("  valCode=========" + valCode);
				} catch (Exception e) {

					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(valCode) || valCode.length() < 4) {
					// System.out.println("CityJiyuanService 验证码识别失败   validataCode=========" + valCode);
					// Thread.sleep(500);
					Loop--;
					continue;
				} else if (valCode.length() > 4) {
					valCode = valCode.substring(0, 4);
				}

				// __VIEWSTATE=/wEPDwUKLTc0OTgwNzU5M2Rk2faTunW9blO97B4Hh3dZuaqxY/S5nDAVkgtNLFtc5+U=&__VIEWSTATEGENERATOR=A3269857
				// &ddlHpzl=02&txtHphm=豫U23088&txtClsbdh=LS5W33BR07B113697&txtYzm=1307&Button1= 查　询
				// __VIEWSTATE=%2FwEPDwUKLTc0OTgwNzU5M2Rk2faTunW9blO97B4Hh3dZuaqxY%2FS5nDAVkgtNLFtc5%2BU%3D
				// &__VIEWSTATEGENERATOR=A3269857
				// &ddlHpzl=02&txtHphm=%D4%A5U23088&txtClsbdh=LS5W33BR07B113697&txtYzm=1307&Button1=+%B2%E9%A1%A1%D1%AF+
				// __VIEWSTATE=%2FwEPDwUJMzgzODA2MDU3D2QWAgIDD2QWAgIBD2QWAmYPFQEj5LuK5aSp5pivMjAxNeW5tDbmnIg05pelICDmmJ%2FmnJ%2Flm5tkZIr4omHJWK3AqYSVvH5EjUeyC3FQxKV3sr4YwyHDEGOT
				// &__VIEWSTATEGENERATOR=5CB615E8&
				// __EVENTTARGET=
				// &Top1_ToolkitScriptManager1_HiddenField=
				// &__EVENTARGUMENT=
				// &ddlHpzl=02&txtHphm=%D4%A5U97178&txtClsbdh=LVHFA152385022802&txtYzm=9753&Button1=+%B2%E9%A1%A1%D1%AF+
				// __VIEWSTATE=%2FwEPDwUJMzgzODA2MDU3D2QWAgIDD2QWAgIBD2QWAmYPFQEj5LuK5aSp5pivMjAxNeW5tDbmnIg05pelICDmmJ%2FmnJ%2Flm5tkZIr4omHJWK3AqYSVvH5EjUeyC3FQxKV3sr4YwyHDEGOT
				// &__VIEWSTATEGENERATOR=5CB615E8&ddlHpzl=02&txtHphm=%D4%A5U97178&txtClsbdh=LVHFA152385022802&txtYzm=5725&Button1=+%B2%E9%A1%A1%D1%AF+
				//String postDate = "__VIEWSTATE=" + URLEncoder.encode(hiddenMap.get("__VIEWSTATE"), "GBK") + "&__VIEWSTATEGENERATOR=" + hiddenMap.get("__VIEWSTATEGENERATOR") + "&ddlHpzl=" + car.getCityPy() + "&txtHphm=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy() + "&txtClsbdh=" + btmId.toUpperCase() + "&txtYzm=" + valCode + "&Button1=+%B2%E9%A1%A1%D1%AF+";
				// System.out.println(postDate);
				String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, GET_VIOLATION_URL, "GBK", next);
				// System.out.println(strRep);
				// System.out.println(vCookies);
				if (StringUtil.isNotEmpty(strRep) && strRep.contains("验证码填写有误")) {
					continue;
				}
				if (StringUtil.isNotEmpty(strRep) && strRep.contains("恭喜您，没有您的违法信息！")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				}
				if (StringUtil.isNotEmpty(strRep) && strRep.contains("根据号牌种类：小型汽车、号牌号码") && strRep.contains("没有找到相关的车辆信息")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("<div id=\"Panel1\">")) {
					ret = parser.parse(strRep, car);
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
			LogUtil.doMkLogData_JGU_With_Msg(car, "err", "验证码识别失败");
		}
		return ret;
	}
}
