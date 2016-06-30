package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.ZmdParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityZmdService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://218.29.234.5:8081/inc/verify.aspx";

	// http://www.hnsqga.gov.cn/mod/grade/oracle.php?hpzl=02&hphm=N0D852&clsbdh=411771&oho_secode=hyxm&&type=9
	private static final String GET_VIOLATION_URL = "http://218.29.234.5:8081/result2.aspx";

	private static final String STRING_REFERER = "http://218.29.234.5:8081/";

	private static final String IMAGE_TYPE = "gif";

	ZmdParser parser = new ZmdParser();

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

	private String lookupViolation(DriverProfile driverProfile, HttpsProxy next) {
		String ret = "";

		int Loop = 0;
		Vector<String> vCookies = new Vector<String>();

		String btmId = driverProfile.getCityPy();
		if (btmId.length() > 5) {
			btmId = btmId.substring(btmId.length() - 5, btmId.length());
		} else if (btmId.length() < 5) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}

		while (Loop < 4) {

			Loop++;
			try {

				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), driverProfile.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, driverProfile.getCityPy());
				// System.out.println("vCookies=="+vCookies);
				// File ff = new File(validataCodeImage);
				// if(!ff.exists()){
				// Loop++;
				// continue;
				// }
				// String newImagePath = filePath + "/" + UUID.randomUUID() + "."+IMAGE_TYPE;
				// ZmdImageFilter imageFilter = new ZmdImageFilter();
				// imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				// File valiDataCodeF = new File(newImagePath);
				// if (!valiDataCodeF.exists()) {
				// Loop++;
				// continue;
				// }
				// String valCode = "";
				//
				// try {
				// valCode = new OCR().recognizeText(valiDataCodeF,IMAGE_TYPE,
				// codeFileP);
				// System.out.println("validataCode=========" + valCode);
				//
				// valCode=valCode.replaceAll("[^0-9]", "");
				//
				// System.out.println("validataCode=========" + valCode);
				//
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// if (!StringUtil.isValid(valCode) || valCode.length() < 4) {
				// continue;
				//
				// } else if (valCode.length() > 4) {
				// valCode = valCode.toUpperCase().substring(0, 4);
				// }
				String cokie = vCookies.get(0);
				String valCode = cokie.replace("Code=", "");
				String postData = "hpzl=" + driverProfile.getCityPy() + "&hphm=" + driverProfile.getCityPy().substring(1, driverProfile.getCityPy().length()) + "&clsbdh=" + btmId + "&yzm=" + valCode + "&Submit3=+%B2%E9+%D1%AF+";
				// System.out.println("postData====="+postData);
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "GBK", next);
				System.out.println("strResp=====" + strResp);
				if (strResp.contains("验证码输入错误")) {
					continue;
				}
				// 根据车辆类型：小型汽车、车牌号：豫QHL638、车辆识别代号：36982，没有找到相关的车辆违法信息！
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("根据车辆类型：小型汽车、车牌号") && strResp.contains("没有找到相关的车辆违法信息")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				}
				if (StringUtil.isNotEmpty(strResp) && !strResp.contains("未处理")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				} else if (strResp.contains("违法信息")) {
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
			LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "其他错误");
		}
		return ret;
	}
}
