package com.mapbar.provincetodo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.HainanParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class HainanService implements Transfer {
	// private static final String VALIDATE_IMAGE_URL="http://www.hainanjj.gov.cn/user/wfcx/AjaxCodeImg.aspx?";
	// http://www.hainanjj.gov.cn/user/wfcx/AjaxCode.aspx?1441532907440&_=1441532907440
	private static final String VALIDATE_CODE = "http://www.hainanjj.gov.cn/user/wfcx/AjaxCode.aspx?";
	private static final String GET_VIOLATION_URL = "http://www.hainanjj.gov.cn/user/wfcx/index.aspx";
	private static final String STRING_REFERER = "http://www.hainanjj.gov.cn/user/wfcx/index.aspx";
	// AjaxCode.aspx?Code=
	private static final String CHECK_CODE = "http://www.hainanjj.gov.cn/user/wfcx/AjaxCode.aspx?Code=";
	// private static final String IMAGE_TYPE = "jpg";

	HainanParser parser = new HainanParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				// strResult = lookupViolation(car, ProxyManager.next(true));
				strResult = lookupViolation(car, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = null;

		Vector<String> vCookies = new Vector<String>();
		String btmId = "";
		if (btmId.length() > 4) {
			btmId = btmId.substring(btmId.length() - 4, btmId.length());
		} else if (btmId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}

		int Loop = 0;

		while (Loop < 1) {
			Loop++;
			try {

				String index = HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "UTF-8");
				Map<String, String> hidden = parser.parseHidden(index);
				System.out.println(hidden);
				System.out.println(vCookies);
				long time = new Date().getTime();
				String validate_code_url = VALIDATE_CODE + time + "&_=" + time;
				String code = HttpsUtils.getURLContentsWithCookies(validate_code_url, vCookies, STRING_REFERER, next, "UTF-8");
				System.out.println("code======" + code);

				String checkcode = HttpsUtils.getURLContentsWithCookies(CHECK_CODE + code, vCookies, STRING_REFERER, next, "UTF-8");
				System.out.println("checkcode======" + checkcode);
				// String valiDateImageUrl = VALIDATE_IMAGE_URL + new Date().getTime();
				// // 校验验证码存储目录是否存在 不存在则创建
				// String filePath = FileHelper.checkDerictor(
				// PropertiesUtils.getProValue("IMAGE_STORE_PATH"),
				// car.strCityPy,
				// String.valueOf(DateUtils.getCurrentDate()));
				//
				//
				// String codeFileP = filePath + "/" + UUID.randomUUID();
				// String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// //得到图片
				// NetHelper.getUrlImageWithCookies(valiDateImageUrl, vCookies,
				// STRING_REFERER, next, validataCodeImage, car.strCityPy);
				//
				// File ff = new File(validataCodeImage);
				// if(!ff.exists()){
				// continue;
				// }
				//
				//
				//
				// String code = "";
				// try {
				// code = new OCR().recognizeText(ff, IMAGE_TYPE,
				// codeFileP);
				// //System.out.println("validataCode=========" + code);
				// code = code.toUpperCase();
				//
				// code = code.replaceAll("[^0-9]", "");
				//
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// if (!StringUtil.isValid(code) || code.length() < 4) {
				// //System.out.println(" HainanService 验证码识别失败   validataCode=========" + code);
				// continue;
				// } else if (code.length() > 4) {
				// code = code.toUpperCase().substring(0, 4);
				// }
				// Hphm:琼B53288
				// HPZL:02
				// Clsbdh:5793
				// Code:8338
				// CheckCode=24821&Hphm=%E7%90%BCA39088&HPZL=02&Clsbdh=6857&Code=3186
				String postDate = "CheckCode=" + checkcode + "&Hphm=" + car.getCityPy() + car.getCityPy() + "&HPZL=" + car.getCityPy() + "&Clsbdh=" + btmId + "&Code=" + code;
				// System.out.println("postDate========="+postDate);
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println("strResp========="+strResp);
				// 验证码失效 验证码不正确！
				if (StringUtil.isNotEmpty(strResp) && (strResp.contains("验证码失效") && strResp.contains("验证码不正确"))) {

					continue;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("恭喜您，您没有违章")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					break;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("网络异常！")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
					break;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("id='xcwfjl'")) {
					ret = parser.parse(strResp, car);
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					break;
				}
			} catch (SocketTimeoutException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接超时");
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "其他错误");
				e.printStackTrace();
			}
		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "其他错误");
		}
		return ret;
	}
}
