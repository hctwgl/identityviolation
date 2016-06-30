package com.mapbar.city.todo;

import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.ShangqiuParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityShangqiuService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.hnsqga.gov.cn/secode.php?b=1&rand=";

	// http://www.hnsqga.gov.cn/mod/grade/oracle.php?hpzl=02&hphm=N0D852&clsbdh=411771&oho_secode=hyxm&&type=9
	private static final String GET_VIOLATION_URL = "http://www.hnsqga.gov.cn/mod/grade/oracle.php?";

	private static final String STRING_REFERER = "http://sqrb.com.cn/sqjj/sqwzcx.htm";

	ShangqiuParser parser = new ShangqiuParser();
	private static final String IMAGE_TYPE = "jpg";

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

			String btmId = car.getCityPy();
			if (btmId.length() > 6) {
				btmId = btmId.substring(btmId.length() - 6, btmId.length());
			} else if (btmId.length() < 6) {
				return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
			}
			Random random2 = new Random(999);

			while (Loop < 3) {

				Loop++;
				try {

					// String urlData="id=2&hpzl=02&hphm="+URLEncoder.encode(car.strState,"UTF-8")+car.strRegId+"&sbdm="+engId;
					// String indexHtml = NetHelper.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "GBK");
					// System.out.println("vCookies=="+vCookies);
					// System.out.println("indexHtml==="+indexHtml);
					String valiDateImageUrl = VALIDATE_IMAGE_URL + random2.nextInt();
					String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

					String codeFileP = filePath + "/" + UUID.randomUUID();
					String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
					// 得到图片
					HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
					// System.out.println("vCookies=="+vCookies);
					// File ff = new File(validataCodeImage);
					// if(!ff.exists()){
					// Loop++;
					// continue;
					// }
					// String newImagePath = filePath + "/" + UUID.randomUUID() + "."+IMAGE_TYPE;
					//
					// File valiDataCodeF = new File(newImagePath);
					// if (!valiDataCodeF.exists()) {
					// Loop++;
					// continue;
					// }
					// String valCode = "";
					//
					// try {
					// valCode = new OCR().recognizeText(valiDataCodeF,IMAGE_TYPE,
					// codeFileP);
					// System.out.println("validataCode=========" + valCode);
					//
					// valCode=valCode.replaceAll("[^a-zA-Z]", "");
					// valCode=valCode.toUpperCase();
					// System.out.println("validataCode=========" + valCode);
					//
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					// if (!StringUtil.isValid(valCode) || valCode.length() < 4) {
					// continue;
					//
					// } else if (valCode.length() > 4) {
					// valCode = valCode.toUpperCase().substring(0, 4);
					// }
					String validataCode = "";
					String[] selectChar = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
					// 所有候选组成验证码的字符，当然也可以用中文的

					for (int i = 0; i < 4; i++) {
						int charIndex = (int) Math.floor(Math.random() * 36);
						validataCode += selectChar[charIndex];
					}
					// http://www.hnsqga.gov.cn/mod/grade/oracle.php?hpzl=02&hphm=NHP197&clsbdh=080410&oho_secode=jsZM&&type=9
					String urlDate = "hpzl=02&hphm=" + car.getCityPy() + "&clsbdh=" + btmId + "&oho_secode=" + validataCode + "&type=9";
					String strResp = HttpsUtils.getURLContentsWithCookies(GET_VIOLATION_URL + urlDate, vCookies, STRING_REFERER, next, "UTF-8");
					System.out.println("strResp=====" + strResp);
					if (strResp.contains("验证码过期")) {
						continue;
					}
					if (strResp.contains("没有匹配的车辆!!!!")) {
						return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
					}
					if (strResp.contains("违法记录未找到")) {
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					} else if (strResp.contains("违法行为代码查询")) {
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						ret = parser.parse(strResp, car);
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
