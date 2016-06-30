package com.mapbar.city.todo;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.AnyangImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.AnyangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityAnyangService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.aycgs.com/GetYzmCode.ashx?t=";

	// http://www.hnsqga.gov.cn/mod/grade/oracle.php?hpzl=02&hphm=N0D852&clsbdh=411771&oho_secode=hyxm&&type=9
	// private static final String GET_VIOLATION_URL = "http://www.aycgs.com/Jdc_cx.aspx?WinCount=0&_=";

	private static final String STRING_REFERER = "http://www.aycgs.com/Jdc_cx.aspx?WinCount=0&_=";

	private static final String IMAGE_TYPE = "jpg";

	AnyangParser parser = new AnyangParser();

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

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = "";
		try {
			int Loop = 0;
			Vector<String> vCookies = new Vector<String>();

			String engId = car.getCityPy();
			if (engId.length() > 4) {
				engId = engId.substring(engId.length() - 4, engId.length());
			} else if (engId.length() < 4) {
				return ResultCache.toErrJsonResult("查询规则变化，请输入发动机号后4位，给您带来的不变，请您谅解！");
			}
			Random random2 = new Random(9999);

			while (Loop < 15) {
				Loop++;

				try {
					String referUrl = STRING_REFERER + Math.random();

					String indexHtml = HttpsUtils.getURLContentsWithCookies(referUrl, vCookies, referUrl, next, "UTF-8");
					Map<String, String> hidden = parser.parseHidden(indexHtml);
					// System.out.println(hidden);
					// System.out.println("vCookies=="+vCookies);
					String valiDateImageUrl = VALIDATE_IMAGE_URL + random2.nextInt();
					String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

					String codeFileP = filePath + "/" + UUID.randomUUID();
					String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
					// 得到图片
					HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, referUrl, next, validataCodeImage, car.getCityPy());
					// System.out.println("vCookies=="+vCookies);
					File ff = new File(validataCodeImage);
					if (!ff.exists()) {

						continue;
					}
					String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
					AnyangImageFilter imageFilter = new AnyangImageFilter();
					imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
					File valiDataCodeF = new File(newImagePath);
					if (!valiDataCodeF.exists()) {

						continue;
					}
					String valCode = "";

					try {
						valCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
						// System.out.println("validataCode=========" + valCode);

						valCode = valCode.replaceAll("[^0-9a-zA-Z]", "");
						valCode = valCode.toUpperCase();
						System.out.println("validataCode=========" + valCode);

					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!StringUtil.isNotEmpty(valCode) || valCode.length() < 4) {

						continue;

					} else if (valCode.length() > 4) {
						valCode = valCode.toUpperCase().substring(0, 4);
					}

					// ddlHpzl:02
					// txtHphm:豫EFM828
					// txtFdjh:9896
					// txtYzm:B3BN
					// Button1: 查　询
					String postData = parser.getParamData(hidden) + "&ddlHpzl=02&txtHphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + car.getCityPy() + "&txtFdjh=" + engId + "&txtYzm=" + valCode + "&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+";

					// System.out.println("postData====="+postData);
					String strResp = HttpsUtils.postURLContentsWithCookies(referUrl, postData, vCookies, referUrl, "UTF-8", next);
					// System.out.println("strResp====="+strResp);
					if (strResp.contains("验证码填写有误', '提示'")) {
						// System.out.println("验证码错误===="+valCode);
						continue;
					}
					if (StringUtil.isNotEmpty(strResp) && strResp.contains("<td align=\"center\">正常</td>")) {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						LogUtil.doMkLogData_jiaoguanProv(car, "ok");
						break;
					} else if (strResp.contains("<div id=\"Panel1\">") && strResp.contains("违法未处理")) {
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						ret = parser.parse(strResp);
						break;
					} else if (strResp.contains("没有找到相关的车辆信息', '提示'")) {
						return ResultCache.toErrJsonResult("车辆信息错误,发动机号或车牌号错误！");
					} else {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						break;
					}

				} catch (Exception e) {
					LogUtil.doMkLogData_jiaoguanju(car, "err");
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// LogHelp.doMkLogData_jiaoguanju(car, "err");
			e.printStackTrace();
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return ret;
	}
}
