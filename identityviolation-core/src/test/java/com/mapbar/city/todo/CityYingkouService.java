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
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.YingkouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityYingkouService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.ykjj.gov.cn/yzheng/code.php";

	private static final String GET_VIOLATION_URL = "http://www.ykjj.gov.cn/wfcx.php";

	private static final String STRING_REFERER = "http://www.ykjj.gov.cn/wfcx.php";
	private static final String IMAGE_TYPE = "png";
	// lei=02&cp=%C1%C9HCS773&dm=ls5a2abe2ea133869&yang=1284&image.x=32&image.y=10&act=1
	YingkouParser parser = new YingkouParser();

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
		while (Loop < 15) {
			try {
				Loop++;

				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());

				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}

				String valCode = "";

				try {
					valCode = new OCR().recognizeText(ff, IMAGE_TYPE, codeFileP);

					valCode = valCode.replaceAll("[^0-9a-zA-Z]", "");
					valCode = valCode.toUpperCase();
					// System.out.println("validataCode=========" + valCode);

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(valCode) || valCode.length() < 4) {
					continue;

				} else if (valCode.length() > 4) {
					valCode = valCode.toUpperCase().substring(0, 4);
				}

				String postData = "lei=02&cp=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy() + "&dm=" + car.getCityPy() + "&yang=" + valCode + "&image.x=32&image.y=10&act=1";

				// System.out.println(postData);
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "GBK", next);
				// System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("验证码错误，请重新填写")) {
					// System.out.println("验证码错误 =="+valCode);
					continue;
				} else if (strResp.contains("无违章记录,请继续保持")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					break;
				} else if (strResp.contains("查询结果：")) {
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
			LogUtil.doMkLogData_JGU_With_Msg(car, "err", "其他错误");
		}
		return ret;
	}
}
