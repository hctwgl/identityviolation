package com.mapbar.traffic.score.transfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.ShenyangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class ShenyangService implements Transfer {

	private static final String VALIDATE_IMAGE_URL = "http://218.25.58.44/searchsys/img2.jsp";
	private static final String GET_VIOLATION_URL = "http://218.25.58.44/search/jsyxxrs.action";
	private static final String STRING_REFERER = "http://218.25.58.44/searchsys/";
	private static final String IMAGE_TYPE = "jpg";

	ShenyangParser parser = new ShenyangParser();

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
		while (Loop < 3) {
			Loop++;
			try {
				List<String> cookies = new ArrayList<String>();
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), driverProfile.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(VALIDATE_IMAGE_URL, cookies, STRING_REFERER, next, validataCodeImage, driverProfile.getCityPy());
				File valiDataCodeF = new File(validataCodeImage);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					code = code.toUpperCase();
					code = code.replaceAll("[^0-9a-zA-Z]", "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}
				// cardno=210102808299&idno=210104199007124323&rand2=7511&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+
				Map<String, String> params = new HashMap<String, String>();
				params.put("cardno", driverProfile.getLssueArchive());
				params.put("idno", driverProfile.getDriverLicense());
				params.put("rand2", code);
				params.put("Button1", URLEncoder.encode(" 查　询 ", "UTF-8"));
				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, cookies, STRING_REFERER, "UTF-8", next);
				//System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("验证码输入有误，请重新输入")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号或档案编号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("没有找到相关信息")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					break;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("class=\"table\"")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					ret = parser.parse(strResp, driverProfile);
					break;
				} else {
					Thread.sleep(1000);
					continue;
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
