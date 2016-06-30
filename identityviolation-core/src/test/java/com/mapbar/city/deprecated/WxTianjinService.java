package com.mapbar.city.deprecated;

import java.io.File;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.WxTianjinImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.WxTianjinParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class WxTianjinService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://111.160.75.94/msfwpt/web.do?method=createCheckCode&flag=0&id=appid&a=";

	private static final String GET_VIOLATION_URL = "http://111.160.75.94/msfwpt/web.do?method=plteQueryIllegalDetailForMob&flag=0&id=appid";
	private static final String STRING_REFERER = "http://111.160.75.94/msfwpt/web.do?method=linkCarIllegalQueryMob&flag=0&id=appid&version=2&sjly=sj";

	private static final String IMAGE_TYPE = "jpg";

	WxTianjinParser tianjinParser = new WxTianjinParser();

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
			Vector<String> vCookies = new Vector<String>();

			int Loop = 0;

			String engId = car.getCityPy();
			if (engId.length() > 6) {
				engId = engId.substring(engId.length() - 6, engId.length());
			} else if (engId.length() < 6) {
				// 查询规则变化，请输入xxxxxx
				return ResultCache.toErrJsonResult("车辆信息错误，请输入发动机号后六位，发动机号的不足6位的请在前面补0，发动机号为“无”的请输入000000 ！");
			}

			while (Loop < 3) {
				Loop++;
				try {

					String rootHtml = HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "GBK");
					System.out.println("vCookies===" + vCookies);

					Pattern p = Pattern.compile("h2\"\\)\\.value\\s=\\s'(.*?)';");
					Matcher match = p.matcher(rootHtml);
					if (match.find()) {
					}

					String valiDateImageUrl = VALIDATE_IMAGE_URL + Math.random();
					// 校验验证码存储目录是否存在 不存在则创建
					String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

					String codeFileP = filePath + "/" + UUID.randomUUID();
					String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
					// 得到图片
					HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
					System.out.println("vCookies===" + vCookies);
					File ff = new File(validataCodeImage);
					if (!ff.exists()) {
						continue;
					}
					WxTianjinImageFilter imageFilter = new WxTianjinImageFilter();
					String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
					imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);

					File valiDataCodeF = new File(newImagePath);
					if (!valiDataCodeF.exists()) {
						continue;
					}

					String code = "";
					try {
						code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
						System.out.println("validataCode=========" + code);
						code = code.replaceAll("[^0-9]", "");
						System.out.println(" WxTianjinService validataCode=========" + code);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
						System.out.println(" WxTianjinService 验证码识别失败   validataCode=========" + code);
						continue;
					} else if (code.length() > 4) {
						code = code.toUpperCase().substring(0, 4);
					}

					String postDate = "hphm=" + car.getCityPy() + "&identification=&fdjh=" + engId + "&cllx=02&captch=" + code;
					System.out.println("postDate==" + postDate);
					String repStr = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "GBK", next);

					System.out.println("repStr==" + repStr);
					if (StringUtil.isNotEmpty(repStr) && repStr.contains("<label id=\"labTitle2\">该车辆无违法记录</label>")) {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						System.out.println("1111");
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						break;
					}
					if (StringUtil.isNotEmpty(repStr) && repStr.contains("id=\"alarmList\"")) {
						System.out.println("22222");
						ret = tianjinParser.parse(repStr, car);
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.doMkLogData_jiaoguanju(car, "err");
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
