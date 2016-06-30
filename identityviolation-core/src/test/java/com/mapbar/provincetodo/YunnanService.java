package com.mapbar.provincetodo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.YunnanParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class YunnanService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://cha.110122.cn/png.aspx?";
	private static final String GET_VIOLATION_URL = "http://cha.110122.cn/";
	private static final String STRING_REFERER = "http://cha.110122.cn/";
	private static final String IMAGE_TYPE = "jpg";

	YunnanParser parser = new YunnanParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, ProxyManager.next(true));
				// strResult = lookupViolation(car,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret = "";

		String btmId = car.getCityPy();
		if (btmId.length() > 4) {
			btmId = btmId.substring(btmId.length() - 4, btmId.length());
		} else if (btmId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}

		int Loop = 0;
		while (Loop < 2) {
			Loop++;
			try {
				Vector<String> vCookies = new Vector<String>();

				//String indexStr = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "UTF-8");
				//Map<String, String> hiddenMap = parser.parseHidden(indexStr);
				System.out.println("YunnanService   vCookies=========" + vCookies);
				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// //得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				System.out.println("YunnanService   vCookies=========" + vCookies);

				// String newImagePath = filePath + "/" + UUID.randomUUID() + "."+IMAGE_TYPE;
				//
				// YunnanImageFilter imageFilter = new YunnanImageFilter();
				// imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				@SuppressWarnings("unused")
				String code = "";
				String checkcode = vCookies.get(0);

				// File valiDataCodeF = new File(validataCodeImage);
				//
				// if (!valiDataCodeF.exists()) {
				//
				// continue;
				// }
				//
				// try {
				// code = new OCR().recognizeText(valiDataCodeF,IMAGE_TYPE,
				// codeFileP);
				//
				// code = code.toLowerCase();
				// code = code.replaceAll("[^0-9]", "");
				// System.out.println("YunnanService   code=========" + code);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// if (!StringUtil.isValid(code) || code.length() < 6) {
				// System.out.println("YunnanService 验证码识别失败   validataCode=========" + code);
				// continue;
				// } else if (code.length() > 6) {
				// code = code.substring(0, 6);
				// }
				code = checkcode.replace("CheckCode=", "");
				// time:2015-01-06 13:38:58
				// hpzl:02
				// hphm:云AE6Z61
				// clsbdh:2446
				// captcha:schuz
				// register:查 询
				Map<String, String> postData = new HashMap<String, String>();
				/*
				 * String postData=parser.getParamDate(hiddenMap, "UTF-8") +"&CarType="+car.getCityPy()+"&Address="+URLEncoder.encode(car.getCityPy(), "UTF-8")+"&hm="+URLEncoder.encode(car.getCityPy(), "UTF-8") +"&lastsixnumber="+btmId+"&Validator="+code+"&Button2=%E6%9F%A5%E8%AF%A2"; //System.out.println(" postData=========" + postData);
				 */String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println(strResp);
				if (strResp.contains("没有违法记录，以交管部门最终认定为准，向遵章守法的您致敬")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				}
				if (strResp.contains("id=\"GridView1\"")) {
					ret = parser.parse(strResp, car);
					LogUtil.doMkLogData_jiaoguanProv(car, "ok");
					break;
				} else if (strResp.contains("车辆识别代号不正确")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				} else {
					Thread.sleep(500);
					continue;
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
