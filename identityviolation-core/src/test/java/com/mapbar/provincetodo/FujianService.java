package com.mapbar.provincetodo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.FujianImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class FujianService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://jjcx.fjgat.gov.cn:800/ValidateImage.ashx?r=";

	private static final String VALIDATECHECK_URL = "http://jjcx.fjgat.gov.cn:800/validateCheck.aspx?code=4vdar&r=";

	private static final String SUBMIT_URL = "http://jjcx.fjgat.gov.cn:800/submit.aspx?";

	private static final String GET_REQ_STATUS = "http://jjcx.fjgat.gov.cn:800/get.aspx?";

//	private static final String GET_VALIDATE_URL = "http://jjcx.fjgat.gov.cn:800/result.aspx?";
	// http://jjcx.fjgat.gov.cn:800/result.aspx?id=c51fbce34ace41ce8ce2f1747d8ccfc7&t=5&r=0.15613724873401225

	private static final String STRING_REFERER = "http://jjcx.fjgat.gov.cn:800/jdcwfcx.aspx";
	private static final String IMAGE_TYPE = "gif";

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

		Vector<String> vCookies = new Vector<String>();

		int Loop = 0;

		String btmId = "";
		if (btmId.length() > 4) {
			btmId = btmId.substring(btmId.length() - 4, btmId.length());
		} else if (btmId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}

		while (Loop < 20) {
			Loop++;
			try {
				String valiDateImageUrl = VALIDATE_IMAGE_URL + Math.random();
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				// System.out.println("vCookies===="+vCookies);
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {

					continue;
				}
				FujianImageFilter imageFilter = new FujianImageFilter();
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {

					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					// System.out.println("validataCode=========" + code);
					code = code.replace("/\\", "A");

					code = code.replace(".I", "J");
					if (code.length() > 5) {
						code = code.replace("L:", "C");
					}
					if (code.length() > 5) {
						code = code.replace("-Y", "x");
					}
					code = code.replace("(,", "6");
					code = code.replace("(;", "6");
					code = code.replace("l{", "K");
					code = code.replace("J{", "K");
					code = code.replace("n'{", "m");
					code = code.replace("£", "F");
					code = code.replace("I‘", "r");
					code = code.replace("i‘", "r");
					code = code.replace("1<", "E");
					code = code.replaceAll("[^0-9a-zA-Z]", "");
					if (code.length() > 5) {
						code = code.replace("11", "H");
					}
					if (code.length() > 5) {
						code = code.replace("II", "H");
					}
					code = code.replaceAll("[^0-9a-zA-Z]", "");
					if (code.length() > 5) {
						code = code.replace("11", "H");
					}
					// System.out.println(" FujianService validataCode=========" + code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 5) {
					// System.out.println(" FujianService 验证码识别失败   validataCode=========" + code);
					continue;
				} else if (code.length() > 5) {
					code = code.toUpperCase().substring(0, 5);
				}

				String checkUrl = VALIDATECHECK_URL + Math.random();

				String strRespCheck = HttpClientUtil.getURLContentsWithCookies(checkUrl, vCookies, STRING_REFERER, next);
				// System.out.println("vCookies===="+vCookies);
				// System.out.println("strRespCheck====="+strRespCheck);
				if (StringUtil.isNotEmpty(strRespCheck) && "true".equals(strRespCheck)) {
					Loop = 20;
					String submiturl = SUBMIT_URL + "q_hpzl=%u95FD" + "" + "&q_hphm=02&q_dn=" + btmId + "&q_sfz=undefined&q_dah=undefined&q_tty=5&q_tks=undefined&q_tke=undefined&code=" + code + "&r=" + Math.random();
					String reqId = HttpClientUtil.getURLContentsWithCookies(submiturl, vCookies, STRING_REFERER, next);
					// System.out.println("reqId====="+reqId);
					int L = 0;
					// http://jjcx.fjgat.gov.cn:800/get.aspx?id=c51fbce34ace41ce8ce2f1747d8ccfc7&r=0.9250466485973448

					String getReqStatusUrl = GET_REQ_STATUS + "id=" + reqId + "&r=" + Math.random();
					while (L < 10) {
						L++;
						String status = HttpClientUtil.getURLContentsWithCookies(getReqStatusUrl, vCookies, STRING_REFERER, next);
						if ("ok".equals(status)) {
							break;
						} else {
							Thread.sleep(1000);
						}
					}
					//String getResult = GET_VALIDATE_URL + "id=" + reqId + "&r=" + Math.random();
					//String strResp = HttpClientUtil.getURLContentsWithCookies(getResult, vCookies, STRING_REFERER, next, "UTF-8");
					// System.out.println(strResp);
				} else {
					continue;
				}
				// http://jjcx.fjgat.gov.cn:800/submit.aspx?q_hpzl=%u95FDD9Z635&q_hphm=02&q_dn=8468&q_sfz=undefined&q_dah=undefined&q_tty=5&q_tks=undefined&q_tke=undefined&code=EGhue&r=0.1871655248105526

				// q_hpzl=%u95FDA9Z635&q_hphm=02&q_dn=8468&q_sfz=undefined&q_dah=undefined&q_tty=5&q_tks=undefined&q_tke=undefined&code=4vdar&r=0.4421110269613564

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
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return ret;
	}
}
