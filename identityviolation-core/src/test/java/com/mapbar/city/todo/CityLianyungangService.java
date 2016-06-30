package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.LianyungangImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.LianyungangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityLianyungangService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://218.92.11.81/codeImg.aspx?flag=";

	private static final String GET_VIOLATION_URL = "http://218.92.11.81/traffic_1.aspx";

	private static final String ROOT_URL = "http://www.lygjg.net/";
	private static final String STRING_REFERER = "http://218.92.11.81/traffic_1.aspx";
	private static final String IMAGE_TYPE = "gif";

	LianyungangParser parser = new LianyungangParser();

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

		int Loop = 0;
		Vector<String> vCookies = new Vector<String>();

		String engId = car.getCityPy();
		if (engId.length() > 6) {
			engId = engId.substring(engId.length() - 6, engId.length());
		} else if (engId.length() < 6) {
			return ResultCache.toErrJsonResult("车辆信息错误,发动机号过短！");
		}

		while (Loop < 5) {
			try {
				Loop++;

				HttpsUtils.getURLContentsWithCookies(ROOT_URL, vCookies, ROOT_URL, next);
				// System.out.println("vCookies=="+vCookies);
				// Map<String,String> hidden = parser.parseHidden(indexHtml, "gbk")
				// System.out.println(indexHtml);

				String html = HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, ROOT_URL, next);
				Map<String, String> hidden = parser.parseHidden(html);
				System.out.println(hidden);
				String valiDateImageUrl = VALIDATE_IMAGE_URL + Math.random();
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());

				LianyungangImageFilter imageFilter = new LianyungangImageFilter();
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {

					continue;
				}
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;

				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {

					continue;
				}
				String valCode = "";

				try {
					valCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					System.out.println("valCode========" + valCode);
				} catch (Exception e) {

					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(valCode) || valCode.length() < 4) {
					Loop--;
					continue;

				} else if (valCode.length() > 4) {
					valCode = valCode.toUpperCase().substring(0, 4);
				}
				System.out.println("vCookies==" + vCookies);
				// String code="";
				// for(String s:vCookies){
				// if(s.contains("CheckCode")){
				// code=s.replace("CheckCode=", "");
				// break;
				// }
				// }

				// __VIEWSTATE=/wEPDwUKLTExNjQ2MzA1OA9kFgICBQ8WAh4LXyFJdGVtQ291bnQCARYCAgEPZBYCZg8VBQnoi49HRFQ3MDYG6ZmI6LeDEjIwMTUtMy0zMSAxMzowMDowMAzog5zliKnkuJzot68n6am+6L2m5pe25pyJ5YW25LuW5aao56KN5a6J5YWo6KGM6L2m55qEZGTtVpkDPZPsT/ZTj3OC8RCoyv7F2g==&__EVENTVALIDATION=/wEWHwKqqLygCgLL9P6yCwLVjrTHDgLEw53oDALEw5HoDALEw5XoDALEw6noDALEw63oDALEw6HoDALEw6XoDALEw7noDALEw/3rDALEw/HrDALbw53oDALbw5HoDALbw5XoDALbw6noDALbw63oDALbw6HoDALbw6XoDALbw7noDALbw/3rDALbw/HrDALaw53oDALaw5HoDALaw5XoDALaw6noDALaw63oDALDw/HrDALdxLb/CQLR1viaCYxgSfad1uZTTin9xUh1Fc5IHerl
				// &NumTextBox=苏GDT706
				// &OwnerTextBox=149585&TypeDropDownList=02&ValidCodePage=NVTA&SearchButton=查询
				// System.out.println("code===="+code);
				String postData = parser.getParamData(hidden, "GBK") + "&NumTextBox=" + URLEncoder.encode(car.getCityPy() + car.getCityPy(), "GBK") + "&OwnerTextBox=" + engId + "&TypeDropDownList=02&ValidCodePage=" + valCode + "&SearchButton=" + URLEncoder.encode("查询", "GBK");

				// System.out.println(postData);
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "GBK", next);
				System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("验证码不正确！")) {
					// System.out.println("验证码错误 =="+code);
					continue;
				} else if (strResp.contains("车主姓名和发动机号不匹配")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,发动机号或车牌号错误！");
				} else if (strResp.contains("没有查到违章信息！")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				} else if (strResp.contains("<table border=1>")) {
					System.out.println("查询成功");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = parser.parse(strResp, car);
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
