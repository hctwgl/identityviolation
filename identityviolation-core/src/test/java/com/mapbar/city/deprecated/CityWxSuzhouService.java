package com.mapbar.city.deprecated;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.WxSuzhouImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.WxSuzhouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class CityWxSuzhouService implements Transfer {

	private static final String VALIDATE_IMAGE_URL = "http://221.224.161.124/tools/verify_code.ashx?time=";

	private static final String GET_VIOLATION_URL = "http://221.224.161.124/weixin/VehicleQuery.aspx?from=app";

	private static final String STRING_REFERER = "http://221.224.161.124/weixin/VehicleQuery.aspx?from=app";

	private static final String IMAGE_TYPE = "png";

	WxSuzhouParser parser = new WxSuzhouParser();

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
		try {
			Vector<String> vCookies = new Vector<String>();
			String btmId = car.getCityPy();
			if (btmId.length() > 7) {
				btmId = btmId.substring(btmId.length() - 7, btmId.length());
			} else if (btmId.length() < 7) {
				return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
			}

			Random random2 = new Random(9999);

			int Loop = 0;
			while (Loop < 15) {

				Loop++;
				try {
					String indexStr = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);

					Map<String, String> hiddenMap = parser.parseHidden(indexStr);
					String paramDate = parser.getParamData(hiddenMap);

					System.out.println(paramDate);
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
					WxSuzhouImageFilter imageFilter = new WxSuzhouImageFilter();
					imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
					File valiDataCodeF = new File(newImagePath);
					if (!valiDataCodeF.exists()) {

						continue;
					}
					String validataCode = "";
					try {
						validataCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
						// System.out.println("  validataCode=========" + validataCode);
						validataCode = validataCode.replace(")(", "X");
						validataCode = validataCode.replace("|i", "6");
						validataCode = validataCode.toLowerCase();
						validataCode = validataCode.replaceAll("[^0-9a-zA-Z]", "");
						// System.out.println("  validataCode=========" + validataCode);
					} catch (Exception e) {

						e.printStackTrace();
					}
					if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 4) {
						System.out.println("CityWxSuzhouService 验证码识别失败   validataCode=========" + validataCode);
						// Thread.sleep(500);
						Loop--;
						continue;
					} else if (validataCode.length() > 4) {
						validataCode = validataCode.substring(0, 4);
					}

					// __VIEWSTATE=%2FwEPDwUKLTc0OTgwNzU5Mw9kFgICAw9kFgICCw8PFgIeB1Zpc2libGVnZGRkUfvAAgP2FmYDJZp50Jo4AoCDLQI%3D
					// &ddlHpzl=02&txtHphm=%E8%B1%ABA567RN&txtClsbdh=LSVCE6A40BN291315&txtYzm=VRFS&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+
					// plate1=%E8%8B%8F&plate2=E&plate3=03F13&vehicleType=02&last7id=7168224&txtVerifyCode=86DR&btnQuery=%E6%AD%A3%E5%9C%A8%E6%9F%A5%E8%AF%A2...
					// String postDate=paramDate+"&plate1=%E8%8B%8F&plate2=E&plate3="+car.getCityPy().substring(1)
					// +"&last7id="+btmId+"&txtVerifyCode="+validataCode+"&btnQuery=%E6%AD%A3%E5%9C%A8%E6%9F%A5%E8%AF%A2...";
					// System.out.println(postDate);
					Map<String, String> postDate = new HashMap<String, String>();
					String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "UTF-8", next);
					System.out.println(strRep);
					System.out.println(vCookies);
					if (StringUtil.isNotEmpty(strRep) && strRep.contains("验证码不正确")) {
						continue;
					}
					if (StringUtil.isNotEmpty(strRep) && strRep.contains("您目前没有违章记录，要继续保持哦")) {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						break;
					} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("您输入的号牌或车辆识别代号有误,请重试")) {
						return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
					} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("class='table1'")) {
						ret = parser.parse(strRep, car);
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						break;
					}

				} catch (Exception e) {
					LogUtil.doMkLogData_jiaoguanju(car, "err");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// LogHelp.doMkLogData_jiaoguanProv(car, "err");
			e.printStackTrace();
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return ret;
	}
}
