package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.ZhengzhouImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.WxZhengzhouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityZhengzhouService implements Transfer {
	// http://bespeak.zzjtgl.cn/zxywbl/jdc_cx.aspx?ywdm=A_1
	// http://bespeak.zzjtgl.cn/zxywbl/GetYzmCode.aspx?t=8614
	// __VIEWSTATE:/wEPDwUKLTc0OTgwNzU5Mw9kFgICAw9kFgICCw8PFgIeB1Zpc2libGVnZGRkUfvAAgP2FmYDJZp50Jo4AoCDLQI=
	// ddlHpzl:02
	// txtHphm:豫A567RN
	// txtClsbdh:LSVCE6A40BN291315
	// txtYzm:m6xg
	// Button1: 查　询

	WxZhengzhouParser parser = new WxZhengzhouParser();
	// Random random2 = new Random(100);
	private static final String VALIDATE_IMAGE_URL = "http://bespeak.zzjtgl.cn/zxywbl/GetYzmCode.aspx?t=";

	private static final String GET_VIOLATION_URL = "http://bespeak.zzjtgl.cn/zxywbl/jdc_cx.aspx?ywdm=A_1";

	private static final String STRING_REFERER = "http://bespeak.zzjtgl.cn/zxywbl/jdc_cx.aspx?ywdm=A_1";

	private static final String IMAGE_TYPE = "jpg";

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

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret = "";

		Vector<String> vCookies = new Vector<String>();
		String btmId = car.getCityPy();

		Random random2 = new Random(9999);

		int Loop = 0;
		while (Loop < 15) {
			Loop++;

			try {
				String indexStr = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);

				Map<String, String> hiddenMap = parser.parseHidden(indexStr);
				String paramDate = parser.getParamData(hiddenMap);
				// System.out.println(paramDate);
				// System.out.println(vCookies);
				String valiDateImageUrl = VALIDATE_IMAGE_URL + random2.nextInt();
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;

				File ff = new File(validataCodeImage);
				if (!ff.exists()) {

					continue;
				}
				ZhengzhouImageFilter imageFilter = new ZhengzhouImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {

					continue;
				}
				String validataCode = "";
				try {
					validataCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);

					validataCode = validataCode.toUpperCase();
					validataCode = validataCode.replaceAll("[^0-9a-zA-Z]", "");
					// System.out.println("  validataCode=========" + validataCode);
				} catch (Exception e) {

					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 4) {
					System.out.println("CityZhengzhouService 验证码识别失败   validataCode=========" + validataCode);
					Loop--;
					continue;
				} else if (validataCode.length() > 4) {
					validataCode = validataCode.substring(0, 4);
				}

				// __VIEWSTATE=%2FwEPDwUKLTc0OTgwNzU5Mw9kFgICAw9kFgICCw8PFgIeB1Zpc2libGVnZGRkUfvAAgP2FmYDJZp50Jo4AoCDLQI%3D
				// &ddlHpzl=02&txtHphm=%E8%B1%ABA567RN&txtClsbdh=LSVCE6A40BN291315&txtYzm=VRFS&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+
				// __VIEWSTATE=%2FwEPDwUKLTc0OTgwNzU5M2RkIT3uvQtItfsX18EUOLY3cKQRa88%3D&ddlHpzl=02&txtHphm=%E8%B1%ABA567RN&txtClsbdh=LSVCE6A40BN291315
				// &txtYzm=G9XW&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+
				// __VIEWSTATE=%2FwEPDwUKLTc0OTgwNzU5M2RkIT3uvQtItfsX18EUOLY3cKQRa88%3D&ddlHpzl=02&txtHphm=%E8%B1%ABA567RN&txtClsbdh=LSVCE6A40BN291315
				// &txtYzm=cwxg&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+
				String postDate = paramDate + "&ddlHpzl=" + car.getCityPy() + "&txtHphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&txtClsbdh=" + btmId + "&txtYzm=" + validataCode + "&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+";
				System.out.println(postDate);
				String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "UTF-8", next);
				System.out.println(strRep);
				if (StringUtil.isNotEmpty(strRep) && strRep.contains("验证码填写有误")) {
					System.out.println("CityZhengzhouService 验证码识别失败 ");
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					continue;
				} else if (strRep.contains("没有找到相关的车辆信息")) {
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				} else if (strRep.contains("id=\"Panel1\"")) {
					ret = parser.parse(strRep);
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
