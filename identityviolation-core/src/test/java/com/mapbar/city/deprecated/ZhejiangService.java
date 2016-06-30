package com.mapbar.city.deprecated;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.ZhejiangImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.ZhejiangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class ZhejiangService implements Transfer {
	//
	private static final String VALIDATE_IMAGE_URL = "http://www.zjsgat.gov.cn:8080/was/Kaptcha.jpg?";
	private static final String CHECK_VALIDATA_CODE = "http://www.zjsgat.gov.cn:8080/was/portals/checkManyYzm.jsp";
	private static final String GET_VIOLATION_URL = "http://www.zjsgat.gov.cn:8080/was/common.do?";
	private static final String STRING_REFERER = "http://www.zjsgat.gov.cn:8080/was/phone/carIllegalQuery.jsp";
	private static final String PAGE_URL = "http://www.zjsgat.gov.cn:8080/was/phone/carIllegalQueryResult.jsp";

	private static final String IMAGE_TYPE = "jpg";

	ZhejiangParser parser = new ZhejiangParser();

	// postdata=tblname=carlllegalquery&flag=gatwsbsdt&carid=%D5%E3A1LD20&cartype=02&carno=752149&yzm=XDDE&laozishiniyeye=laozishiniyeye

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				// strResult = lookupViolation(car,ProxyManager.next(true));
				strResult = lookupViolation(car, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;

	}

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = "";

		Vector<String> vCookies = new Vector<String>();
		String btmId = car.getCityPy();
		if (btmId.length() > 6) {
			btmId = btmId.substring(btmId.length() - 6, btmId.length());
		} else if (btmId.length() < 6) {
			return null;
		}
		int Loop = 0;
		Random random2 = new Random(99);
		while (Loop < 15) {
			Loop++;
			try {

				if (Loop == 1) {
					HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "GBK");
				}

				String valiDateImageUrl = VALIDATE_IMAGE_URL + random2.nextInt(99);
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());

				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;

				ZhejiangImageFilter imageFilter = new ZhejiangImageFilter();
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}
				int tt = imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);

				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String validataCode = "";
				try {
					validataCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					System.out.println("ZhejiangService validataCode=========" + validataCode);
					validataCode = validataCode.replace("%", "S");
					validataCode = validataCode.replace("\\N", "W");
					validataCode = validataCode.replace(".5", "B");
					validataCode = validataCode.replace("§", "S");
					validataCode = validataCode.replace("$\"", "F");
					validataCode = validataCode.replace(":-J", "3");
					validataCode = validataCode.replace("'v'", "W");

					// validataCode = validataCode.replace("|", "I");
					validataCode = validataCode.toUpperCase();
					validataCode = validataCode.replaceAll("[^0-9a-zA-Z]", "");

				} catch (Exception e) {
					e.printStackTrace();
				}
				boolean ifright = true;
				if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 4) {
					System.out.println("ZhejiangService 验证码识别失败   validataCode=========" + validataCode);
					Loop--;
					ifright = false;
				} else if (validataCode.length() > 4) {
					validataCode = validataCode.substring(0, 4);
				}
				String checkCodeRep = "";
				if (ifright) {
					checkCodeRep = HttpsUtils.postURLContentsWithCookies(CHECK_VALIDATA_CODE, "randValue=" + validataCode, vCookies, STRING_REFERER, "GBK", next);
				}

				// System.out.println("vCookies =========" + vCookies);
				System.out.println("checkCodeRep =========" + checkCodeRep);

				if ((!ifright || checkCodeRep.contains("\"result\":\"N\"")) && tt == 3) {

					imageFilter.dealBlack2(validataCodeImage, newImagePath, IMAGE_TYPE);
					try {
						validataCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
						// System.out.println("ZhejiangService validataCode=========" + validataCode);
						validataCode = validataCode.replace("%", "S");
						validataCode = validataCode.replace("\\N", "W");
						validataCode = validataCode.replace(".5", "B");
						validataCode = validataCode.replace("§", "S");
						validataCode = validataCode.replace("$\"", "F");
						validataCode = validataCode.replace(":-J", "3");
						// validataCode = validataCode.replace("|", "I");
						validataCode = validataCode.toUpperCase();
						validataCode = validataCode.replaceAll("[^0-9a-zA-Z]", "");

					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 4) {
						// System.out.println("ZhejiangService 验证码识别失败   validataCode=========" + validataCode);
						continue;
					} else if (validataCode.length() > 4) {
						validataCode = validataCode.substring(0, 4);
					}
					checkCodeRep = HttpsUtils.postURLContentsWithCookies(CHECK_VALIDATA_CODE, "randValue=" + validataCode, vCookies, STRING_REFERER, "GBK", next);
					System.out.println("black second checkCodeRep =========" + checkCodeRep);
				}
				// System.out.println("vCookies======="+vCookies);
				if (checkCodeRep.contains("\"result\":\"Y\"")) {
					// Loop = 100;
					LogUtil.doMkLogData_jiaoguanProv(car, "ok");
					// tblname=carlllegalqurey&flag=gatwsbsdt&carid=%D5%E3AQ892U&cartype=02&carno=002145&yzm=DDAA&laozishiniyeye=laozishiniyeye

					// tblname=carlllegalquery&flag=gatwsbsdt&carid=%D5%E3AQ892U&cartype=02&carno=002145&yzm=A5TS&laozishiniyeye=laozishiniyeye
					// tblname=carlllegalforphonequery&carid=%25E6%25B5%2599AQ892U&carno=002145&cartype=02&carTypeValue=%D0%A1%D0%CD%C6%FB%B3%B5&yzm=TSW3
					String postDate = "tblname=carlllegalforphonequery&carid=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy() + "&cartype=" + car.getCityPy() + "&carno=" + btmId + "&yzm=" + validataCode + "&carTypeValue=%D0%A1%D0%CD%C6%FB%B3%B5";
					System.out.println("postDate=======" + postDate);
					String strRep = HttpsUtils.getURLContentsWithCookies(GET_VIOLATION_URL + postDate, vCookies, STRING_REFERER, next, "GBK");
					System.out.println("strRep=======" + strRep);
					if (strRep.contains("车辆无非现场违法记录")) {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
						break;
					}

					Pattern p = Pattern.compile("共<span class=\"red\">(\\d+)</span>条记录");
					Matcher match = p.matcher(strRep);
					int datanum = 0;
					if (match.find()) {
						datanum = new Integer(match.group(1));
					}
					System.out.println(datanum);
					// strRep = NetHelper.getURLContentsWithCookies("http://www.zjsgat.gov.cn:8080/was/phone/carIllegalQueryResult.jsp", vCookies, "http://www.zjsgat.gov.cn:8080/was/phone/carIllegalQuery.jsp", next,"GBK");
					Document doc = Jsoup.parse(strRep, "UTF-8");
					Elements tables = doc.getElementsByTag("table");

					Elements trs = tables.get(0).getElementsByTag("tr");
					DriverCase vc = new DriverCase();
					JSONArray jcases = new JSONArray();
					JSONObject jcase = null;
					if (trs != null && trs.size() > 0) {

						String time = trs.get(4).getElementsByTag("td").get(1).text();

						String dizhi = trs.get(2).getElementsByTag("td").get(1).text();

						String con = trs.get(3).getElementsByTag("td").get(1).text();

						String status = trs.get(5).getElementsByTag("td").get(1).text();

						jcase = new JSONObject();
						jcase.put("序号", "1");
						jcase.put("违法时间", time);
						jcase.put("违法地点", dizhi);
						jcase.put("违法内容", con);
						jcase.put("记分", "未知");
						jcase.put("罚款", "未知");
						jcase.put("处理情况", status);
						jcases.add(jcase);
					}
					for (int i = 1; i < datanum; i++) {
						String url = PAGE_URL + "?currentpage=" + i;
						String strRepPage = HttpsUtils.getURLContentsWithCookies(url, vCookies, PAGE_URL, next, "GBK");
						Document docPage = Jsoup.parse(strRepPage, "UTF-8");
						Elements tablesPage = docPage.getElementsByTag("table");

						Elements trspage = tablesPage.get(0).getElementsByTag("tr");

						if (trspage != null && trspage.size() > 0) {
							String time = trspage.get(4).getElementsByTag("td").get(1).text();

							String dizhi = trspage.get(2).getElementsByTag("td").get(1).text();

							String con = trspage.get(3).getElementsByTag("td").get(1).text();

							String status = trspage.get(5).getElementsByTag("td").get(1).text();

							jcase = new JSONObject();
							jcase.put("序号", (i + 1) + "");
							jcase.put("违法时间", time);
							jcase.put("违法地点", dizhi);
							jcase.put("违法内容", con);
							jcase.put("记分", "未知");
							jcase.put("罚款", "未知");
							jcase.put("处理情况", status);
							jcases.add(jcase);
						}
					}
					if (jcases.size() > 0) {
						vc.json.put("data", jcases);

						vc.json.put("count", jcases.size());

						vc.json.put("status", "ok");

						ret = vc.toString();
					} else {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");

					break;

				} else {
					// System.out.println("ZhejiangService 验证码识别失败   validataCode=========" + validataCode);
					// Thread.sleep(500);
					continue;
				}
			} catch (SocketTimeoutException e) {

				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接超时");
				e.printStackTrace();
			} catch (SocketException e) {

				LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
				break;

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

			continue;
		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "验证码识别失败");
		}
		return ret;
	}
}
