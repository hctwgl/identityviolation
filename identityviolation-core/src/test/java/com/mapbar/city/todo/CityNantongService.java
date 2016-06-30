package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.NantongImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.NantongParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityNantongService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.ntjxj.com/InternetWeb/GetValidateImg";

	private static final String GET_VIOLATION_URL = "http://www.ntjxj.com/InternetWeb/QueryDzjkSevlet";

	private static final String ROOT_URL = "http://www.ntjxj.com/InternetWeb/index.jsp";
	private static final String STRING_REFERER = "http://www.ntjxj.com/InternetWeb/dzjk_check.jsp";

	NantongParser parser = new NantongParser();
	private static final String IMAGE_TYPE = "jpg";

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

		int Loop = 0;
		Vector<String> vCookies = new Vector<String>();

		String engId = car.getCityPy();
		if (engId.length() > 4) {
			engId = engId.substring(engId.length() - 4, engId.length());
		} else if (engId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,发动机号过短！");
		}

		while (Loop < 18) {

			Loop++;
			try {

				String startDate = "id=2&hpzl=02&hphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&sbdm=" + engId;
				 HttpsUtils.postURLContentsWithCookies(STRING_REFERER, startDate, vCookies, ROOT_URL, "UTF-8", next);
				// System.out.println("vCookies=="+vCookies);
				// System.out.println(indexHtml);
				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, "http://www.ntjxj.com/InternetWeb/validimagepage.jsp", next, validataCodeImage, car.getCityPy());
				// System.out.println("vCookies=="+vCookies);
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {

					continue;
				}
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				NantongImageFilter imageFilter = new NantongImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				//
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {

					continue;
				}
				String valCode = "";

				try {
					valCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					// System.out.println("validataCode=========" + valCode);

					valCode = valCode.replace("\\*\\", "H");
					valCode = valCode.replace("\\'\\", "H");
					valCode = valCode.replace("/~/", "H");
					valCode = valCode.replace("l~/", "H");
					valCode = valCode.replace("/‘I", "H");
					valCode = valCode.replace("/V", "N");
					valCode = valCode.replace("Y>", "B");
					valCode = valCode.replace("/I’", "X");
					valCode = valCode.replace("/\\’", "X");
					valCode = valCode.replace("/|’", "X");
					valCode = valCode.replace("«|’", "X");
					valCode = valCode.replace(".{’", "X");
					valCode = valCode.replace("/?", "R");
					valCode = valCode.replace("I?", "R");
					valCode = valCode.replace("1?", "R");

					valCode = valCode.replace("o", "0");
					valCode = valCode.replace("/3", "P");
					valCode = valCode.replace("I/", "V");
					valCode = valCode.replace("l/", "V");
					valCode = valCode.replace("\\l", "V");
					valCode = valCode.replace(")’", "Y");
					valCode = valCode.replace("}’", "Y");
					valCode = valCode.replace("/(", "K");
					valCode = valCode.replace("7‘", "T");
					valCode = valCode.replace("P\\", "A");
					valCode = valCode.replace("6’", "B");
					valCode = valCode.replace("‘L", "Z");
					valCode = valCode.replace("’L", "2");
					valCode = valCode.replace("%", "8");
					valCode = valCode.replace("£", "E");
					// DH’L£
					valCode = valCode.replaceAll("[^0-9a-zA-Z]", "");
					valCode = valCode.toUpperCase();
					// System.out.println("validataCode=========" + valCode);

				} catch (Exception e) {

					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(valCode) || valCode.length() < 4) {
					Loop--;
					continue;

				} else if (valCode.length() > 4) {
					valCode = valCode.toUpperCase().substring(0, 4);
				}

				String postData = "id=2&zjhm=&dabh=&hpzl=" + car.getCityPy() + "&hphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&sbdm=" + engId + "&yzm=" + valCode;
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
				System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("验证码不正确！")) {
					// System.out.println("验证码错误 =="+valCode);
					continue;
				} else if (strResp.contains("录入错误或不存在此车号")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,发动机号或车牌号错误！");
				} else if (strResp.contains("没有查询到该车的违法行为")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				} else if (strResp.contains("<div id=\"box\">")) {
					// System.out.println("查询成功");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = parser.parse(strResp, car);
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
