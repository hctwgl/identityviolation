package com.mapbar.traffic.score.transfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpHost;

import com.alibaba.fastjson.JSONArray;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.HeilongjiangImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.HeilongjiangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class HeilongjiangService implements Transfer {

	private static final String VALIDATE_IMAGE_URL = "http://www.hljjj.gov.cn:8081/Home/Yzm?time=";
	private static final String GET_VIOLATION_URL = "http://www.hljjj.gov.cn:8081/Home/Jfcx";
	private static final String STRING_REFERER = "http://www.hljjj.gov.cn:8081/Home/Jfcx";
	private static final String IMAGE_TYPE = "jpg";

	HeilongjiangParser parser = new HeilongjiangParser();

	@Override
	public String checkDriverScore(DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (driverProfile != null && StringUtil.isNotEmpty(driverProfile.getCityPy())) {
				strResult = lookupScore(driverProfile, ProxyManager.next(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;

	}

	private String lookupScore(DriverProfile driverProfile, HttpHost next) {
		String ret = "";
		List<String> cookies = new ArrayList<String>();
		int Loop = 0;
		while (Loop < 25) {
			Loop++;
			try {
				String valiDateImageUrl = VALIDATE_IMAGE_URL + new Date().getTime();
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), driverProfile.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, cookies, STRING_REFERER, next, validataCodeImage, driverProfile.getCityPy());
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}
				HeilongjiangImageFilter imageFilter = new HeilongjiangImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String validataCode = "";
				try {
					validataCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					if (validataCode.length() > 5) {
						validataCode = validataCode.replace("13", "B");
						validataCode = validataCode.replace("II", "H");
					}
					validataCode = validataCode.replace("e", "B");
					validataCode = validataCode.replace("$", "8");
					validataCode = validataCode.replace("¢", "2");
					validataCode = validataCode.toUpperCase();
					validataCode = validataCode.replaceAll("[^0-9a-zA-Z]", "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 5) {
					Loop--;
					continue;
				} else if (validataCode.length() > 5) {
					validataCode = validataCode.substring(0, 5);
				}
				// cao=undefined&yzm=646z8&hpzl=undefined&dy=undefined&xzqh=undefined&hphm=undefined&clsbdh=undefined&jszh=231084199312162925&dabh=231000771359&jkbj=undefined&ts=1450070641887
				Map<String, String> params = new HashMap<String, String>();
				params.put("yzm", validataCode);
				params.put("jszh", driverProfile.getDriverLicense());
				params.put("dabh", driverProfile.getLssueArchive());
				params.put("ts", new Date().getTime() + "");
				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, cookies, STRING_REFERER, "UTF-8", next);
				// System.out.println(strResp);
				String html = "";
				if (strResp.contains("验证码错误")) {
					if (validataCode.contains("B")) {
						validataCode = validataCode.replace("B", "6");
					}
					if (validataCode.contains("A")) {
						validataCode = validataCode.replace("A", "4");
					}
					params.put("yzm", validataCode);
					strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, cookies, STRING_REFERER, "UTF-8", next);
					if (strResp.contains("验证码错误")) {
						continue;
					} else {
						JSONArray jsonarr = JSONArray.parseArray(strResp);
						html = jsonarr.getString(2);
					}
				} else if (strResp.contains("驾驶证号或档案编号填写错误")) {
					LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号或档案编号错误！");
					break;
				} else if (strResp.contains("驾驶证号")) {
					JSONArray jsonarr = JSONArray.parseArray(strResp);
					html = jsonarr.getString(2);
				}
				if (!"".equals(html)) {
					LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "ok", "");
					ret = parser.parse(html, driverProfile);
					break;
				} else {
					continue;
				}
			} catch (SocketTimeoutException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "网络连接超时");
				e.printStackTrace();
			} catch (SocketException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
				break;
			} catch (FileNotFoundException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "其他错误");
				e.printStackTrace();
			}
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "验证码识别失败");
		}
		return ret;
	}
}
