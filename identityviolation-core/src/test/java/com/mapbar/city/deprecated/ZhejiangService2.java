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
public class ZhejiangService2 implements Transfer {
	//
	private static final String VALIDATE_IMAGE_URL = "http://www.zjsgat.gov.cn:8080/was/Kaptcha.jpg?";
	private static final String CHECK_VALIDATA_CODE = "http://www.zjsgat.gov.cn:8080/was/portals/checkManyYzm.jsp";
	private static final String GET_VIOLATION_URL = "http://www.zjsgat.gov.cn:8080/was/common.do";
	private static final String STRING_REFERER = "http://www.zjsgat.gov.cn:8080/was/portals/car_lllegal_query.jsp";

	//private static final String get_cookie = "http://www.zjsgat.gov.cn:8080/was/portals/getCookie.jsp";

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
					String postDate = "tblname=carlllegalquery&flag=gatwsbsdt&carid=" + URLEncoder.encode(car.getCityPy(), "GBK") + car.getCityPy() + "&cartype=02&carno=" + btmId + "&yzm=" + validataCode + "&laozishiniyeye=laozishiniyeye";
					System.out.println("postDate=======" + postDate);
					String strRep = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "GBK", next);
					System.out.println("strRep=======" + strRep);
					if (strRep.contains("由于未知原因，查询出现异常")) {
						return ResultCache.toErrJsonResult("车辆信息错误,车牌号或者车架号错误！");
					}

					if (strRep.contains("车辆没有查询到非现场违法记录")) {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						LogUtil.doMkLogData_jiaoguanProv(car, "ok");
						break;
					}
					if (strRep.contains("&nbsp;记录&nbsp;1")) {
						ret = parser.parse(strRep, car);
						LogUtil.doMkLogData_jiaoguanProv(car, "ok");
						break;
					}

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
