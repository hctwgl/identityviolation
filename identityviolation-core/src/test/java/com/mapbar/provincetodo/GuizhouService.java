package com.mapbar.provincetodo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.GuizhouImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.GuizhouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class GuizhouService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.gzjjzd.gov.cn/zhcx/yzm.php?";
	private static final String GET_VIOLATION_URL = "http://www.gzjjzd.gov.cn/zhcx/rejkjl.php";
	private static final String STRING_REFERER = "http://www.gzjjzd.gov.cn/zhcx/jkjl.php";
	private static final String ROOT_URL = "http://www.gzjjzd.gov.cn/search.php";
	private static final String IMAGE_TYPE = "png";
	GuizhouParser parser = new GuizhouParser();

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
		Random random2 = new Random(9);
		Vector<String> vCookies = new Vector<String>();
		// String tempData = "hpzl=02&hphm="+car.strState+car.strRegId+"&fdjh="+engId+"&code="+random2.nextInt(9)+random2.nextInt(9)+random2.nextInt(9)+random2.nextInt(9);
		String session = "";
		while (Loop < 15) {
			Loop++;
			try {

				if (vCookies.isEmpty()) {
					String temp = HttpClientUtil.getURLContentsWithCookies(ROOT_URL, vCookies, ROOT_URL, next);
					// System.out.println(temp);
					Pattern p = Pattern.compile("Verify=(.*?)\"");
					Matcher match = p.matcher(temp);

					if (match.find()) {
						session = match.group(1);
						// System.out.println("macth=="+);
					}
				}
				// String sessionStr=
				// if(vCookies.isEmpty()){
				// NetHelper.postURLContentsWithCookies(GET_VIOLATION_URL,
				// tempData, vCookies, STRING_REFERER, "UTF-8", next);
				// }

				// http://www.gzjjzd.gov.cn/zhcx/yzm.php?5286.904789912300&WebShieldSessionVerify=IZqbqLoYMZjOYfV5D0TB

				String getImageUrl = VALIDATE_IMAGE_URL + random2.nextInt(9) + random2.nextInt(9) + random2.nextInt(9) + random2.nextInt(9) + String.valueOf(Math.random()).substring(1, 14);

				if (!session.isEmpty()) {
					getImageUrl += "&WebShieldSessionVerify=" + session;
				}

				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;

				// 得到图片

				HttpClientUtil.getUrlImageWithCookies(getImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;

				// 处理图片去除图片干扰线
				GuizhouImageFilter imageFilter = new GuizhouImageFilter();
				boolean ifOk = imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				if (!ifOk) {
					continue;
				}
				File valiDataCodeF = new File(newImagePath);
				// ImageIOHelper.createImage(new File(validataCodeImage), IMAGE_TYPE, newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);

					code = code.toUpperCase();

					code = code.replaceAll("[^0-9a-zA-Z]", "");
					// System.out.println(" code=========" + code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					// System.out.println("GuizhouService 验证码识别失败   validataCode=========" + code);
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}
				//
				//String postData = "hpzl=" + car.getCityPy() + "&hphm=" + car.getCityPy() + car.getCityPy() + "&fdjh=" + engId + "&code=" + code;
				// System.out.println(" postData=========" + postData);
				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println("vCookies=="+vCookies);
				// System.out.println("strResp1=="+strResp);
				if (!StringUtil.isNotEmpty(strResp)) {
					continue;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("当前没有交通违法记录，请继续保持")) {
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("您的车牌不正确,或车牌与发动机号不相符")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,发动机号或车牌号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && (strResp.contains("验证码错误") || strResp.contains("服务维护中"))) {
					continue;
				} else {
					if (strResp.contains("WebShieldSessionVerify=")) {
						continue;
						// Pattern p = Pattern.compile("Verify=(.*?)\"");
						// Matcher match = p.matcher(strResp);
						//
						// if (match.find()){
						// String tempSession = match.group(1);
						// strResp = NetUtil.postURLContentsWithCookies(GET_VIOLATION_URL+"?WebShieldSessionVerify="+tempSession,
						// postData, vCookies, STRING_REFERER, "UTF-8", next);
						// System.out.println("strResp2=="+strResp);
						// if(strResp.contains("JumpSelf()")){
						// strResp = NetUtil.postURLContentsWithCookies(GET_VIOLATION_URL,
						// postData, vCookies, STRING_REFERER, "UTF-8", next);
						// System.out.println("strResp=="+strResp);
						// }
						// }
					}
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
			LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "验证码识别失败");
		}
		return ret;
	}
}
