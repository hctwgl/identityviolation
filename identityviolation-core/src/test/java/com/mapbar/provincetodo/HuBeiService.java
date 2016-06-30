package com.mapbar.provincetodo;

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

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.HuBeiParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class HuBeiService implements Transfer {
	// http://xxcx.hbsjg.gov.cn:8087/hbjj/imageServlet1?temp=1436750362583
	private static final String VALIDATE_IMAGE_URL = "http://xxcx.hbsjg.gov.cn:8087/hbjj/imageServlet1?temp=";
	// http://xxcx.hbsjg.gov.cn:8087/hbjj/validateServlet?flag=1&randcode=PR0K
	private static final String CHECK_VALIDATA_CODE = "http://xxcx.hbsjg.gov.cn:8087/hbjj/validateServlet?flag=1&randcode=";
	private static final String GET_VIOLATION_URL = "http://xxcx.hbsjg.gov.cn:8087/hbjj/querydzjc.do";
	private static final String STRING_REFERER = "http://xxcx.hbsjg.gov.cn:8087/hbjj/";

	private static final String IMAGE_TYPE = "jpg";

	HuBeiParser parser = new HuBeiParser();

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
		String ret = "";

		List<String> vCookies = new ArrayList<String>();
		String btmId = "";
		if (btmId.length() > 5) {
			btmId = btmId.substring(btmId.length() - 5, btmId.length());
		} else if (btmId.length() < 5) {
			return null;
		}
		int Loop = 0;
		while (Loop < 15) {
			Loop++;
			try {
				if (vCookies.isEmpty()) {
					HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
					// System.out.println("vCookies init     =========" + vCookies);
				}

				String valiDateImageUrl = VALIDATE_IMAGE_URL + new Date().getTime();
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				// System.out.println("vCookies     =========" + vCookies);
				// String newImagePath = filePath + "/" + UUID.randomUUID() + "."+IMAGE_TYPE;
				// File ff = new File(validataCodeImage);
				// if(!ff.exists()){
				// continue;
				// }
				// HuBeiImageFilter imageFilter = new HuBeiImageFilter();
				// imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				// File valiDataCodeF = new File(newImagePath);
				// if (!valiDataCodeF.exists()) {
				// continue;
				// }
				// System.out.println("vCookies     =========" + vCookies);
				// String validataCode = "";
				// try {
				// validataCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE,
				// codeFileP);
				//
				// if(validataCode.length()>4 && validataCode.contains("N|")){
				// validataCode = validataCode.replace("N|", "M");
				// }
				// if(validataCode.contains("q")){
				// validataCode = validataCode.replace("q", "9");
				// }
				// validataCode = validataCode.replace("|", "I");
				// validataCode = validataCode.toUpperCase();
				// validataCode = validataCode.replaceAll("[^0-9a-zA-Z]", "");
				//
				// System.out.println("HuBeiService    validataCode=========" + validataCode);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// if (!StringUtil.isValid(validataCode) || validataCode.length() < 4) {
				// System.out.println("HuBeiService 验证码识别失败   validataCode=========" + validataCode);
				// continue;
				// } else if (validataCode.length() > 4) {
				// validataCode = validataCode.substring(0, 4);
				// }
				// RANDOMVALIDATECODEKEY1=TC7T, RANDOMVALIDATECODEKEY1=""
				String validataCode = "";
				String a = "";
				for (String s : vCookies) {
					if (s.contains("RANDOMVALIDATECODEKEY1") && !s.contains("\"\"")) {
						validataCode = s.replace("RANDOMVALIDATECODEKEY1=", "");
					} else {
						a = s;
					}
				}
				vCookies.remove(a);
				// System.out.println("vCookies =========" + vCookies);
				// System.out.println("validataCode =========" + validataCode);
				String checkCodeRep = HttpClientUtil.getURLContentsWithCookies(CHECK_VALIDATA_CODE + validataCode, vCookies, STRING_REFERER, next);

				// System.out.println("checkCodeRep =========" + checkCodeRep);

				if ("SUCCESS".equals(checkCodeRep)) {
					// queryType=1&brandType=02&areaForShort=%E9%84%82&carNum=A8LJ39&carAuthCode=73139&captcha=TZLM
					// String postDate = "queryType=1&brandType="+car.getCityPy()+"&areaForShort="+car.getCityPy()+"&carNum="+car.getCityPy()+"&carAuthCode="+btmId+"&captcha="+validataCode;
					Map<String, String> postData = new HashMap<String, String>();
					String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
					System.out.println("strRep =========" + strRep);
					if (strRep.contains("您输入的车牌号码或车辆识别码后五位不正确")) {
						LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
						return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
					}
					if (strRep.contains("最近违章次数:0次")) {
						LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						break;
					}
					ret = parser.parse(strRep, car);
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					break;
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
