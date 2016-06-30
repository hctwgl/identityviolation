package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.HanzhongImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.HanzhongParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityHanzhongService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://123.139.173.124:9086/wfcx/imageServlet?";

	// http://123.139.173.124:9086/wfcx/query.do?actiontype=vioSurveil&hpzl=02&hphm=FV9383&tj=FDJH&tj_val=122130194&jdccode=j2Re
	private static final String GET_VIOLATION_URL = "http://123.139.173.124:9086/wfcx/query.do?actiontype=vioSurveil";

	private static final String STRING_REFERER = "http://123.139.173.124:9086/wfcx/jdccx.jsp";

	private static final String IMAGE_TYPE = "jpg";
	HanzhongParser parser = new HanzhongParser();

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

		String engId = car.getCityPy();
		if (engId == null || "".equals(engId) || "选填".equals(engId)) {
			ResultCache.toErrJsonResult("车辆信息错误,发动机号不能为空！");
		}

		Vector<String> vCookies = new Vector<String>();
		while (Loop < 12) {
			Loop++;

			try {

				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				// System.out.println("vCookies====="+vCookies);
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				HanzhongImageFilter imageFilter = new HanzhongImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {

					continue;
				}
				String valCode = "";

				try {
					valCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					// System.out.println("validataCode=========" + valCode);

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

				String urlData = "&hpzl=" + car.getCityPy() + "&hphm=" + car.getCityPy() + "&tj=FDJH&tj_val=" + car.getCityPy() + "&jdccode=" + valCode;

				System.out.println("urlData=====" + urlData);
				String strResp = HttpsUtils.getURLContentsWithCookies(GET_VIOLATION_URL + urlData, vCookies, STRING_REFERER, next, "GBK");
				System.out.println("strResp=====" + strResp);

				if (strResp.contains("验证码输入有误，请登录支队网站查询违法信息")) {
					System.out.println("验证码错误====" + valCode);
					continue;
				}
				if (strResp.contains("无违法信息") || strResp.contains("正常</td>")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("违法未处理</td>")) {
					ret = parser.parse(strResp);
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				} else if (strResp.contains("发动机号匹配错误，请仔细核对") || strResp.contains("号牌号码和号牌种类输入错误,请仔细核对！")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,发动机号或车牌号错误！");
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
