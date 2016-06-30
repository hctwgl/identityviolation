package com.mapbar.city.todo;

import java.io.File;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;

import com.alibaba.fastjson.JSONArray;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.XiamenImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.XiamenParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityXiamenService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://xmitcc.xmjj.gov.cn/jtwf/ClinicCountManager/captcha-image.do?";

	private static final String GET_VIOLATION_URL = "http://xmitcc.xmjj.gov.cn/jtwf/vio/jdc/jdcwfcx";
	private static final String GET_VIOLATION_URL_NEXT = "http://xmitcc.xmjj.gov.cn/jtwf/vio/jdc/all";
	// /jtwf/vio/jdc/all?
	private static final String STRING_REFERER = "http://xmitcc.xmjj.gov.cn/jtwf/vio/index";

	private static final String IMAGE_TYPE = "jpg";
	XiamenParser parser = new XiamenParser();

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
		try {
			Vector<String> vCookies = new Vector<String>();
			String btmId = car.getCityPy();
			if (btmId.length() > 6) {
				btmId = btmId.substring(btmId.length() - 6, btmId.length());
			} else if (btmId.length() < 6) {
				return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
			}

			int Loop = 0;
			while (Loop < 15) {
				Loop++;
				try {

					// System.out.println(vCookies);
					String valiDateImageUrl = VALIDATE_IMAGE_URL + new Date().getTime();
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
					XiamenImageFilter imageFilter = new XiamenImageFilter();
					imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
					File valiDataCodeF = new File(newImagePath);
					if (!valiDataCodeF.exists()) {

						continue;
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
						// System.out.println("CityXiamenService 验证码识别失败   validataCode=========" + code);
						// Thread.sleep(500);
						Loop--;
						continue;
					} else if (code.length() > 4) {
						code = code.substring(0, 4);
					}

					// __VIEWSTATE=%2FwEPDwUKLTc0OTgwNzU5Mw9kFgICAw9kFgICCw8PFgIeB1Zpc2libGVnZGRkUfvAAgP2FmYDJZp50Jo4AoCDLQI%3D
					// &ddlHpzl=02&txtHphm=%E8%B1%ABA567RN&txtClsbdh=LSVCE6A40BN291315&txtYzm=VRFS&Button1=+%E6%9F%A5%E3%80%80%E8%AF%A2+
					// plate1=%E8%8B%8F&plate2=E&plate3=03F13&vehicleType=02&last7id=7168224&txtVerifyCode=86DR&btnQuery=%E6%AD%A3%E5%9C%A8%E6%9F%A5%E8%AF%A2...
					// hpzl=02&sfjc=%E9%97%BD&dm=D&hphm=9Z635&clsbdh=008468&code=9576&jkbj=0
					//String postDate = "hpzl=02&sfjc=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + "&dm=" + car.getCityPy().substring(0, 1) + "&hphm=" + car.getCityPy().substring(1) + "&clsbdh=" + btmId + "&code=" + code + "&jkbj=0";
					// System.out.println(postDate);
					String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "UTF-8", next);
					// System.out.println(strRep);
					// System.out.println(vCookies);

					if (StringUtil.isNotEmpty(strRep) && strRep.contains("您输入的验证码不正确，请重新输入")) {
						continue;
					} else if (strRep.contains("暂无车辆违法信息")) {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						break;
					} else if (strRep.contains("您输入的车辆识别代号后六位不正确，请重新输入")) {
						return ResultCache.toErrJsonResult("车辆信息错误,车架号错误！");

					} else if (strRep.contains("您输入的车辆信息不正确，请重新输入")) {
						return ResultCache.toErrJsonResult("车辆信息错误,车牌号错误！");
					} else if (StringUtil.isNotEmpty(strRep)) {
						Pattern p = Pattern.compile("red\">(\\d+)</span>条记录");
						Matcher match = p.matcher(strRep);
						int n = 0;
						int datanum = 0;
						if (match.find()) {
							// System.out.println("macth=="+match.group(1));
							datanum = new Integer(match.group(1));
							n = datanum / 8;
							if ((datanum % 8) > 0) {
								n++;
							}
						} else {
							continue;
						}
						JSONArray jcases = new JSONArray();
						parser.parse(strRep, jcases, 0);
						if (n > 1) {
							String nextdata = "hpzl=02&hphm=" + car.getCityPy() + car.getCityPy() + "&clsbdh=" + btmId;
							for (int i = 2; i <= n; i++) {
								String refer = HttpClientUtil.getURLContentsWithCookies(GET_VIOLATION_URL_NEXT + "?" + nextdata + "&pageNo=" + i, vCookies, STRING_REFERER, next, "UTF-8");
								// System.out.println(refer);
								parser.parse(refer, jcases, (i - 1) * 8);
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
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						break;
					}
					// break;
				} catch (Exception e) {
					LogUtil.doMkLogData_jiaoguanju(car, "err");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return ret;
	}
}
