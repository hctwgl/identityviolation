package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.ImageIOHelper;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.ChangzhouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityChangzhouService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://czjxj.czinfo.net/psn.asp";

	private static final String GET_VIOLATION_URL = "http://czjxj.czinfo.net/dzjc_result_free.asp";

	private static final String STRING_REFERER = "http://czjxj.czinfo.net/dzjccx.asp";

	private static final String IMAGE_TYPE = "BMP";
	ChangzhouParser parser = new ChangzhouParser();

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

		String engId = car.getCityPy();
		if (engId.length() > 6) {
			engId = engId.substring(engId.length() - 6, engId.length());
		} else if (engId.length() < 6) {
			return ResultCache.toErrJsonResult("车辆信息错误,发动机号过短！");
		}

		int Loop = 0;
		while (Loop < 10) {
			Loop++;

			try {
				Vector<String> vCookies = new Vector<String>();
				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				// System.out.println("vCookies========="+vCookies);
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {

					continue;
				}
				String newImagePath = filePath + "/" + UUID.randomUUID() + ".jpg";

				File valiDataCodeF = ImageIOHelper.createImage(new File(validataCodeImage), IMAGE_TYPE, newImagePath);
				if (!valiDataCodeF.exists()) {

					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, "jpg", codeFileP);
					// System.out.println("validataCode=========" + code);
					code = code.toUpperCase();
					code = code.replace(" ", "");

					code = code.replaceAll("[^0-9]", "");
					// System.out.println("validataCode=========" + code);
				} catch (Exception e) {

					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					// System.out
					// .println("验证码识别失败   validataCode=========" + code);
					Loop--;
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}

				// LCNNM=小型汽车&shengfen=苏D&chepai=J097W&fadongji=62754W&vcode=8174&Submit=查 询
				// LCNNM=%D0%A1%D0%CD%C6%FB%B3%B5&shengfen=%CB%D5D&chepai=M725U&fadongji=210118&vcode=4061&Submit=%B2%E9++%D1%AF
				//String postData = "LCNNM=" + URLEncoder.encode("小型汽车", "GBK") + "&shengfen=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy().substring(0, 1) + "&chepai=" + car.getCityPy().substring(1) + "&fadongji=" + engId + "&vcode=" + code + "&Submit=%B2%E9++%D1%AF";
				// System.out.println("postData========="+postData);
				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "GBK", next);
				// System.out.println("vCookies========="+vCookies);
				// System.out.println("strResp========="+strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("请输入正确的验证码")) {
					// System.out.println("验证码识别失败 CityChangzhouService  validataCode=========" + code);
					continue;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("没有未处理电子警察违法记录")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("您输入的发动机号后六位") && strResp.contains("不匹配，请仔细核对")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,发动机编号错误！");
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("条未处理电子警察违法记录")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = parser.parse(strResp);
					break;
				} else {
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
