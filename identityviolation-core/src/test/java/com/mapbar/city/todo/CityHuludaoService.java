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
import com.mapbar.traffic.score.image.ImageIOHelper;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.HuludaoParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityHuludaoService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.hldgajjzd.com/getcode.asp";

	private static final String GET_VIOLATION_URL = "http://www.hldgajjzd.com/sjcx/sjcx.asp";

	private static final String STRING_REFERER = "http://www.hldgajjzd.com/sjcx/ShowClass.asp?ClassID=16";
	private static final String IMAGE_TYPE = "BMP";
	HuludaoParser parser = new HuludaoParser();

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

		String btmId = car.getCityPy();
		if (btmId == null || "".equals(btmId) || "选填".equals(btmId)) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号不能为空");
		}

		while (Loop < 8) {
			try {
				Loop++;
				Vector<String> vCookies = new Vector<String>();
				// String indexHtml = NetHelper.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
				// System.out.println("vCookies=="+vCookies);
				// System.out.println(indexHtml);

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
				String newImagePath = filePath + "/" + UUID.randomUUID() + ".jpg";

				File valiDataCodeF = ImageIOHelper.createImage(new File(validataCodeImage), IMAGE_TYPE, newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String validataCode = "";
				try {
					validataCode = new OCR().recognizeText(valiDataCodeF, "jpg", codeFileP);

					validataCode = validataCode.toLowerCase();
					validataCode = validataCode.replaceAll("[^0-9a-zA-Z]", "");
					// System.out.println("  validataCode=========" + validataCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 4) {
					System.out.println("CityWxSuzhouService 验证码识别失败   validataCode=========" + validataCode);
					// Thread.sleep(500);
					Loop--;
					continue;
				} else if (validataCode.length() > 4) {
					validataCode = validataCode.substring(0, 4);
				}

				// hphm1=%C1%C9P59J03&hpzl=02&clsbdh=LJ8B2C3DXCD011200&yzm=6131&Submit=%CC%E1%BD%BB
				String postData = "hphm1=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy() + "&hpzl=" + car.getCityPy() + "&clsbdh=" + btmId + "&yzm=" + validataCode + "&Submit=%CC%E1%BD%BB";
				System.out.println(postData);
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "GBK", next);
				System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("验证码不正确，请重新输入")) {
					continue;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("您输入的信息有误，请重新输入")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				} else if (strResp.contains("../Images/jjzd/没违章.gif")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				} else if (strResp.contains("您查询的是车辆号牌:")) {

					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = parser.parse(strResp);
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
