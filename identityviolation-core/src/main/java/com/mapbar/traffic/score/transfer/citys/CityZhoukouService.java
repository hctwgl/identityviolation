package com.mapbar.traffic.score.transfer.citys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.ZhoukouImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.ZhoukouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityZhoukouService implements Transfer {

	private static final String VALIDATE_IMAGE_URL = "http://www.zkgajj.cn/TrafficInfo/getCode.jsp";
	private static final String GET_VIOLATION_URL = "http://www.zkgajj.cn/TrafficInfo/TrafficQueryDriver.action";
	private static final String STRING_REFERER = "http://www.zkgajj.cn/TrafficInfo/TrafficQueryInput.action";
	private static final String IMAGE_TYPE = "jpg";

	ZhoukouParser parser = new ZhoukouParser();

	@Override
	public String checkDriverScore(DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (driverProfile != null && StringUtil.isNotEmpty(driverProfile.getCityPy())) {
				// strResult = lookupViolation(car,ProxyManager.next(true));
				strResult = lookupScore(driverProfile, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupScore(DriverProfile driverProfile, HttpHost next) {
		String ret = "";
		int Loop = 0;
		List<String> vCookies = new ArrayList<String>();
		while (Loop < 10) {
			Loop++;
			try {
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), driverProfile.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));
				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(VALIDATE_IMAGE_URL, vCookies, STRING_REFERER, next, validataCodeImage, driverProfile.getCityPy());
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				ZhoukouImageFilter imageFilter = new ZhoukouImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String valCode = "";
				try {
					valCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					valCode = valCode.replaceAll("[^0-9a-zA-Z]", "");
					valCode = valCode.toUpperCase();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(valCode) || valCode.length() < 4) {
					Loop--;
					continue;
				} else if (valCode.length() > 4) {
					valCode = valCode.toUpperCase().substring(0, 4);
				}
				// String postData = "carframeNO=" + btmId + "&cp2=" + driverProfile.getCityPy().substring(1, driverProfile.getCityPy().length()) + "&checkNO2=" + valCode;
				// xm=%E5%88%98%E6%B0%B8%E6%88%90&sfzhm=412727198910031256&checkNO1=kfh3
				Map<String, String> params = new HashMap<String, String>();
				params.put("xm", driverProfile.getDriverName());
				params.put("sfzhm", driverProfile.getDriverLicense());
				params.put("checkNO1", valCode);
				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, vCookies, STRING_REFERER, "UTF-8", next);
				//System.out.println(strResp);
				if (strResp.contains("验证码输入错误!")) {
					continue;
				}
				if (strResp.contains("进行查询时系统出现错误,请稍后再试")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "交管局故障");
					return null;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("<b>状态:</b><span>")) {
					ret = parser.parse(strResp, driverProfile);
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					break;
				} else if (strResp.contains("驾驶员信息没有找到，请确认您的输入!")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号或姓名错误！");
				}
			} catch (SocketTimeoutException e) {
				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接超时");
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "其他错误");
				e.printStackTrace();
			}
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "验证码识别失败");
		}
		return ret;
	}
}
