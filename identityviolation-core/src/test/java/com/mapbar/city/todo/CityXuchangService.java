package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.XuchangImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.XuchangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityXuchangService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.xcjjw.gov.cn/inc/checkcode.asp";

	private static final String GET_VIOLATION_URL = "http://www.xcjjw.gov.cn/weizhang.asp";

	private static final String STRING_REFERER = "http://www.xcjjw.gov.cn/weizhang.asp";

	private static final String IMAGE_TYPE = "gif";

	XuchangParser parser = new XuchangParser();

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
		while (Loop < 6) {
			Loop++;
			try {

				Vector<String> vCookies = new Vector<String>();
				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;

				// 得到图片
				HttpsUtils.getUrlImageGIFWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());

				// System.out.println("vCookies==="+vCookies);

				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				XuchangImageFilter imageFilter = new XuchangImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);

				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					// System.out.println("validataCode=========" + code);
					code = code.replace("$", "3");
					code = code.replace("Z", "2");
					code = code.replace("Z", "2");
					code = code.replace("G", "6");
					code = code.replace("O", "0");
					code = code.replaceAll("[^0-9]", "");

					System.out.println("validataCode=========" + code);

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					continue;

				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}
				// Authentication=open&types=2&license=KDU811&Submit=ok&search_type=ill&Form_Code=5879&image.x=34&image.y=11
				// Authentication=open&types=2&license=KDU811&Submit=ok&search_type=ill&Form_Code=5718&image.x=22&image.y=8
				// Authentication=open&types=2&license=KDU811&Submit=ok&search_type=ill&Form_Code=4559&image.x=35&image.y=10&s=y
				// Authentication=y&types=2&license=KDU811&Submit=y&search_type=ill&Form_Code=6493&image.x=24&image.y=9&s=y
				String postData = "Authentication=y&types=2&license=" + car.getCityPy() + "&Submit=ok&search_type=ill&Form_Code=" + code + "&image.x=34&image.y=11&s=y";
				// System.out.println("postData==="+postData);
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "gbk", next);
				// System.out.println("vCookies==="+vCookies);
				// System.out.println("strResp====="+strResp);
				if (strResp.contains("没有违法信息")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("查询验证码输入错误，请重新输入")) {
					// System.out.println("查询验证码输入错误!");
					continue;
				} else if (strResp.contains("违法查询结果")) {
					ret = parser.parse(strResp);
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
