package com.mapbar.provincetodo;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.AnhuiImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.AnhuiParser;
import com.mapbar.traffic.score.parser.XiAnParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class AnhuiService implements Transfer {
	// http://www.gzjd.gov.cn:8008/cgs/captcha/getCaptcha.htm?type=2&d=1417069983578
	private static final String VALIDATE_IMAGE_URL = "http://www.ah122.cn/query/CarIllegalLookup.aspx";
	// http://www.cdjg.gov.cn/WebService/OnlineWork/QueryDrvOrVeh/QueryVehDetail.aspx
	private static final String GET_VIOLATION_URL = "http://www.ah122.cn/query/CarIllegalLookup.aspx";
	private static final String CHECK_CODE_URL = "http://www.ah122.cn/query/CarIllegalLookup.aspx?";
	private static final String STRING_REFERER = "http://www.ah122.cn/query/CarIllegalLookup.aspx";

	AnhuiParser parser = new AnhuiParser();
	// http://117.36.53.122:9081/wfcx/query.do?actiontype=vioSurveil&code=ac5de2513d8cedd2265722614bcad71d&hpzl=02&hphm=A0H202&tj=FDJH&tj_val=66495110528826&jdccode=K1H8
	// http://117.36.53.122:9081/wfcx/query.do?actiontype=vioSurveil&code=ac5de2513d8cedd2265722614bcad71d&hpzl=02&hphm=A76B22&tj=FDJH&tj_val=C118472&jdccode=XGJC
	private static final String IMAGE_TYPE = "gif";

	XiAnParser parse = new XiAnParser();

	@Override
	public String checkDriverScore(DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (driverProfile != null && StringUtil.isNotEmpty(driverProfile.getCityPy())) {
				strResult = lookupViolation(driverProfile, ProxyManager.next(true));
				// strResult = lookupViolation(car, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret = "";

		String btmId = car.getClassName();
		if (btmId.length() > 6) {
			btmId = btmId.substring(btmId.length() - 6, btmId.length());
		} else if (btmId.length() < 6) {
			return null;
		}
		List<String> vCookies = new ArrayList<String>();

		int Loop = 0;
		while (Loop < 3) {
			Loop++;

			// String code = MD5Util.MD5(DateUtils.getCurrentDateStr());
			try {
				String html = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
				// System.out.println(vCookies);
				Document doc = Jsoup.parse(html, "utf-8");
				Elements hiddens = doc.getElementsByAttributeValue("type", "hidden");
				Map<String, String> hiddenMap = new HashMap<String, String>();
				for (int i = 0; i < hiddens.size(); i++) {
					Element ele = hiddens.get(i);
					String key = ele.attr("name");
					String value = ele.attr("value");
					// System.out.println(key+":"+value);
					hiddenMap.put(key, value);
				}
				// System.out.println(hiddenMap);

				Element img = doc.getElementById("txtValidatCode$codeText$img");
				String onclick = img.attr("onclick");

				String imageurl = VALIDATE_IMAGE_URL + onclick.split("'")[1] + Math.random();

				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(imageurl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				AnhuiImageFilter imageFilter = new AnhuiImageFilter();
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

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 4) {
					// System.out.println("AnhuiService 验证码识别失败   validataCode=========" + validataCode);
					continue;
				} else if (validataCode.length() > 4) {
					validataCode = validataCode.toUpperCase().substring(0, 4);
				}
				// ___command=GetIsValidate&___param=8192&___clientRandom=0.05098976078443229
				String validataCodeCheckUrl = CHECK_CODE_URL + "___command=GetIsValidate&___param=" + validataCode + "&___clientRandom=" + Math.random();

				// ctl00$CPHQueryForm$CarIllegalRecords1$txtNumber:皖AL2M38
				// ctl00$CPHQueryForm$CarIllegalRecords1$ddlCategory:02
				// ctl00$CPHQueryForm$CarIllegalRecords1$txtCode:216662
				// txtValidatCode$codeText:0886
				// ctl00$CPHQueryForm$CarIllegalRecords1$ibtnSearch.x:38
				// ctl00$CPHQueryForm$CarIllegalRecords1$ibtnSearch.y:15
				String checkCodeRep = HttpClientUtil.getURLContentsWithCookies(validataCodeCheckUrl, vCookies, STRING_REFERER, next);
				// System.out.println("checkCodeRep=="+checkCodeRep);
				if ("True".equalsIgnoreCase(checkCodeRep)) {
					//String paramDate = parse.getParamData(hiddenMap);
					// String postDate ="";
					// String postDate = paramDate+"&ctl00$CPHQueryForm$CarIllegalRecords1$txtNumber="+car.strState+car.strRegId
					// +"&ctl00$CPHQueryForm$CarIllegalRecords1$ddlCategory="+car.carType+"&ctl00$CPHQueryForm$CarIllegalRecords1$txtCode="+btmId+
					// "&txtValidatCode$codeText="+validataCode+
					// "&ctl00$CPHQueryForm$CarIllegalRecords1$ibtnSearch.x=38&ctl00$CPHQueryForm$CarIllegalRecords1$ibtnSearch.y=15";
					Map<String, String> postDate = new HashMap<String, String>();
					String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "utf-8", next);
					// System.out.println("strResp=="+strResp);
					if (StringUtil.isNotEmpty(strResp) && strResp.contains("登记证书或车辆识别代号输入错误")) {
						LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "信息输入错误");
						return ResultCache.toErrJsonResult("车辆信息错误,车架号或发动机号错误！");
					} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("操作超时 请联系管理员")) {
						LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "交管局故障");
						return ResultCache.toErrJsonResult("交管局故障 请稍后再试！");

					} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("目前没有未处理违法信息")) {

						LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
						return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

					} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("现在未处理违法信息")) {

						ret = parser.parse(strResp, car);
						LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
						break;
					}

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
			LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "验证码识别失败");
		}
		return ret;
	}
}
