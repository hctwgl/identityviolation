package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.FuzhouImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.FuzhouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityFuzhouService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://fzjj.easdo.com/jtwf/image.jsp";

	private static final String GET_VIOLATION_URL = "http://fzjj.easdo.com/jtwf/queryWfInfo.jsp";

	private static final String STRING_REFERER = "http://fzjj.easdo.com/jtwf/";

	private static final String IMAGE_TYPE = "jpg";

	FuzhouParser parser = new FuzhouParser();

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
		String btmId = car.getCityPy();
		if (btmId.length() > 4) {
			btmId = btmId.substring(btmId.length() - 4, btmId.length());
		} else if (btmId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}
		Vector<String> vCookies = new Vector<String>();

		// num=A&carNum=JU008&carType=小型汽车&clsbdm=2386&rand=2545
		int Loop = 0;
		while (Loop < 10) {
			Loop++;
			try {

				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;

				File ff = new File(validataCodeImage);
				if (!ff.exists()) {

					continue;
				}
				FuzhouImageFilter imageFilter = new FuzhouImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {
					return null;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);

					code = code.toLowerCase();
					code = code.replaceAll("[^0-9a-zA-Z]", "");
					// System.out.println("  code=========" + code);
				} catch (Exception e) {

					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					// System.out.println("CityFuzhouService 验证码识别失败   validataCode=========" + code);
					// Thread.sleep(500);
					Loop--;
					continue;
				} else if (code.length() > 4) {
					code = code.substring(0, 4);
				}

				// __VIEWSTATE=%2FwEPDwUKLTc0OTgwNzU5Mw9kFgICAw9kFgICCw8PFgIeB1Zpc2libGVnZGRkUfvAAgP2FmYDJZp50Jo4AoCDLQI%3D
				// &ddlHpzl=02&txtHphm=%E8%B1%ABA567RN&txtClsbdh=LSVCE6A40BN291315&txtYzm=VRFS&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+
				// plate1=%E8%8B%8F&plate2=E&plate3=03F13&vehicleType=02&last7id=7168224&txtVerifyCode=86DR&btnQuery=%E6%AD%A3%E5%9C%A8%E6%9F%A5%E8%AF%A2...
				String postDate = "num=" + car.getCityPy().substring(0, 1) + "&carNum=" + car.getCityPy().substring(1) + "&carType=" + URLEncoder.encode("小型汽车", "GBK") + "&clsbdm=" + btmId + "&rand=" + code;
				// System.out.println(postDate);
				String strRep = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "gbk", next);
				// System.out.println("vCookie======="+vCookies);
				// System.out.println(strRep);
				if (StringUtil.isNotEmpty(strRep) && strRep.contains("车辆识别代号错误!请参照行驶证以下位置的识别代号输入")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				}
				if (StringUtil.isNotEmpty(strRep) && strRep.contains("class=\"table-sr\"")) {
					Pattern p = Pattern.compile("您最近总共有(\\d+)项未处理的违法");
					Matcher match = p.matcher(strRep);
					int n = 0;
					int datanum = 0;
					if (match.find()) {
						// System.out.println("macth=="+match.group(1));
						datanum = new Integer(match.group(1));
						n = datanum / 5;
						if ((datanum % 5) > 0) {
							n++;
						}
					}
					if (datanum == 0) {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						break;
					}
					JSONArray jcases = new JSONArray();
					parser.parse(strRep, jcases, 0, vCookies, STRING_REFERER);
					if (n > 1) {

						for (int i = 2; i <= n; i++) {
							String refer = HttpsUtils.getURLContentsWithCookies(GET_VIOLATION_URL + "?" + postDate + "&pageNum=" + i, vCookies, STRING_REFERER, next, "gbk");
							// System.out.println(refer);
							parser.parse(refer, jcases, (i - 1) * 5, vCookies, STRING_REFERER);
						}
					}
					DriverCase vc = new DriverCase();
					if (jcases.size() > 0) {
						vc.json.put("data", jcases);

						vc.json.put("count", jcases.size());

						vc.json.put("status", "ok");

						ret = vc.toString();
					} else {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}
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
