package com.mapbar.provincetodo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpHost;
import org.jdesktop.swingx.util.OS;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.GuangzhouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class GuangzhouService implements Transfer {

	// protected static String strProxyFile = System.getProperty("PROXY_FILE", "https_proxy.txt");
	// protected static ProxyServerManager pMgr = new ProxyServerManager(strProxyFile);
	// http://www.gzjd.gov.cn:8008/cgs/captcha/getCaptcha.htm?type=2&d=1417069983578
	// http://www.gzjd.gov.cn:8008/cgs/captcha/getCaptcha.htm?type=2&d=
	// http://www.gzjd.gov.cn:8008/cgs/captcha/testValidateCaptcha.htm?type=2&d=1417770966172
	// http://www.gzjd.gov.cn:8008/cgs/vehiclelicense/checkVisitorVehicle.htm
	// http://www.gzjd.gov.cn:8008/cgs/captcha/getCaptcha.htm?type=2&d=
	// http://www.gzjd.gov.cn:8008/cgs/captcha/testValidateCaptcha.htm?type=2&d=
	// http://www.gzjd.gov.cn/cgs/captcha/getCaptcha.htm?type=2&d=
	private static final String VALIDATE_IMAGE_URL = "http://www.gzjd.gov.cn/cgs/captcha/getCaptcha.htm?type=2&d=";
	private static final String CHECK_VALIDATA_URL = "http://www.gzjd.gov.cn/cgs/captcha/testValidateCaptcha.htm?type=2&d=";
	private static final String GET_KEY_URL = "http://www.gzjd.gov.cn/cgs/vehiclelicense/checkVisitorVehicle.htm";
	private static final String GET_VIOLATION_URL = "http://www.gzjd.gov.cn/cgs/violation/getVisitorVioList.htm?";
	private static final String STRING_REFERER = "http://www.gzjd.gov.cn/cgs/html/violation/visitor.html";
	private static final String STRING_REFERER2 = "http://www.gzjd.gov.cn/cgs/html/violation/visitor_violation.shtml";
	private static final String IMAGE_TYPE = "jpg";
	private GuangzhouParser parser = new GuangzhouParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, ProxyManager.next(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String strResp = null;

		Long d = new Date().getTime();
		List<String> vCookies = new ArrayList<String>();
		String key = "";
		int Loop = 0;

		String engId = "";
		if (engId.length() > 4) {
			engId = engId.substring(engId.length() - 4, engId.length());
		} else if (engId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,发动机号过短！");
		}

		String btmId = "";
		if (btmId.length() > 6) {
			btmId = btmId.substring(btmId.length() - 6, btmId.length());
		} else if (btmId.length() < 6) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}

		while (Loop < 15) {
			Loop++;
			try {

				//String getKeyStr = "hpzl=" + car.getCityPy() + "&hphm=" + car.getCityPy() + car.getCityPy() + "&fdjh=" + engId + "&clsbdh=" + btmId + "&captcha=%code%";

				String valiDateImageUrl = VALIDATE_IMAGE_URL + d;
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				// String newImagePath = filePath + "/" + UUID.randomUUID() + "."+IMAGE_TYPE;

				// 对图片中值滤波
				// GuangzhouImageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(validataCodeImage);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP, getOcrCommend(valiDataCodeF, codeFileP));
					// System.out.println("validataCode=========" + code);
					code = code.toUpperCase();
					code = code.replaceAll(" ", "");
					code = code.replace("5", "S");
					code = code.replace("8", "S");
					code = code.replace("}", "J");
					code = code.replaceAll("[^0-9a-zA-Z]", "");

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					// System.out.println("GuangzhouService 验证码识别失败   validataCode=========" + code);
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}
				// captchaId:DXOO "captchaId="+code
				Map<String, String> postData = new HashMap<String, String>();
				String checkCodeRep = HttpClientUtil.postURLContentsWithCookies(CHECK_VALIDATA_URL + new Date().getTime(), postData, vCookies, STRING_REFERER, "UTF-8", next);

				if (checkCodeRep.contains("\"returnMessage\":\"fail\"")) {
					// System.out.println(" 验证码验证失败");
					Thread.sleep(100);
					continue;
				}
				// System.out.println("vCookies  ==========" + vCookies);
				// getKeyStr = replaceToken(getKeyStr, "code", code);
				Map<String, String> postDate = new HashMap<String, String>();
				strResp = HttpClientUtil.postURLContentsWithCookies(GET_KEY_URL, postDate, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println("guangzhou strResp==========" + strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.startsWith("\"5_error")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或发动机号号错误！");
				} else if (!StringUtil.isNotEmpty(strResp)) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车牌号码错误！");
				}

				if (StringUtil.isNotEmpty(strResp) && strResp.contains("key:") && !strResp.contains("$0$0")) {

					strResp = strResp.replaceAll("\"", "");
					key = strResp.substring(strResp.indexOf("key") + 4, strResp.lastIndexOf("$"));
					if (key.contains("$")) {
						key = key.substring(0, key.lastIndexOf("$"));
					}
					String num = strResp.substring(strResp.lastIndexOf("$") + 1);

					if (new Integer(num) == 0) {

						LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");

						return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}

					// return key;
					// platenumtype=02&platenum=%25E7%25B2%25A4AM8H52&engineno=7904&vehicleidnum=019191&key=tYXvdzzJKTIA87MKYSpn+79gwOdaXTyf2CLttWFIHrZebf1QMji18g==
					/*
					 * String getViolitonStr = "platenumtype=02&platenum=" + URLEncoder.encode(car.getCityPy(),"utf-8")+ car.getCityPy() + "&engineno=" + engId + "&vehicleidnum=" + btmId + "&key=" + URLDecoder.decode(key,"utf-8");
					 */
					Map<String, String> getViolitonStr = new HashMap<String, String>();
					strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, getViolitonStr, vCookies, STRING_REFERER2, "UTF-8", next);

					// System.out.println("guangzhou result ==========" + strResp);

					if (strResp.contains("\"returnCode\":\"0\"")) {
						LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
						return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					} else {
						LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
						return parser.parse(strResp, car);
					}
				} else {
					strResp = "";
				}
				Thread.sleep(500);

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

		return null;
	}

	private static List<String> getOcrCommend(File imageFile, String codeFilePath) {
		List<String> cmd = new ArrayList<String>();
		if (OS.isWindowsXP()) {
			cmd.add(OCR.tessPath + "tesseract");
		} else if (OS.isLinux()) {
			cmd.add("tesseract");
		} else {
			cmd.add(OCR.tessPath + "tesseract.exe");
		}
		cmd.add(imageFile.getPath());
		cmd.add(codeFilePath);
		cmd.add("-psm");
		cmd.add("6");
		cmd.add(OCR.LANG_OPTION);
		cmd.add("eng");
		return cmd;
	}

	@SuppressWarnings("unused")
	private static String replaceToken(String BUY_XML, String strKey, String strValue) {
		String strRet = BUY_XML;

		try {
			if (StringUtil.isNotEmpty(strRet) && StringUtil.isNotEmpty(strKey)) {
				String strKeyToken = "%" + strKey + "%";
				strRet = strRet.replaceAll(strKeyToken, strValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;
	}
}
