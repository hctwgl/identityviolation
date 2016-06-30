package com.mapbar.provincetodo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.NanjingImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.NanjingParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class NanjingService implements Transfer {

	private static final String VALIDATE_IMAGE_URL = "http://www.njjg.gov.cn:81/GetValidate.aspx?n=4&t=";
	private static final String GET_VIOLATION_URL = "http://www.njjg.gov.cn:81/simplequery/simplequery.aspx?";
	private static final String STRING_REFERER = "http://www.njjg.gov.cn:81/simplequery/simplequery.aspx";
	private static final String IMAGE_TYPE = "png";

	NanjingParser parser = new NanjingParser();

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

		String engId = car.getCityPy();
		if (engId.length() > 6) {
			engId = engId.substring(engId.length() - 6, engId.length());
		} else if (engId.length() < 6) {
			return ResultCache.toErrJsonResult("车辆信息错误,发动机号过短！");
		}
		int Loop = 0;
		while (Loop < 5) {
			Loop++;
			try {
				Vector<String> vCookies = new Vector<String>();

				//String indexStr = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
				//Map<String, String> hiddenMap = parser.parseHidden(indexStr);

				String valiDateImageUrl = VALIDATE_IMAGE_URL + new Date().getTime();
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;

				// 处理图片去除图片干扰线
				NanjingImageFilter imageFilter = new NanjingImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				// ImageIOHelper.createImage(new File(validataCodeImage), IMAGE_TYPE, newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					// System.out.println("NanjingService validataCode=========" + code);
					code = code.toUpperCase();

					code = code.replaceAll("[^0-9a-zA-Z]", "");

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					// System.out.println("NanjingService 验证码识别失败   validataCode=========" + code);
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}

				String regId = car.getCityPy();
				//String regIdPre = car.getCityPy() + regId.substring(0, 1);

				regId = regId.substring(1, regId.length());

				//String todate = DateUtils.getCurrentDateStr();
				//String preDate = DateUtils.getPreDate(-2, Calendar.YEAR);

				//String hiddenDate = parser.getParamData(hiddenMap);
				//String postData = hiddenDate + "&lsb_PlateNo=02&txt_hp1=" + URLEncoder.encode(regIdPre) + "&txt_hp2=" + regId + "&txt_fdjh=" + engId + "&txtCheck=" + code + "&btn_Query=%E6%9F%A5+%E8%AF%A2" + "&txt_BDate=" + preDate + "&txt_EDate=" + todate;
				// System.out.println("postDate========="+postData);

				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "UTF-8", next);
				System.out.println("strResp=========" + strResp);

				if (StringUtil.isNotEmpty(strResp) && strResp.contains("发动机号不正确")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,发动机号或车牌号错误！");
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("id=\"dtgCar\"")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = parser.parse(strResp, car);
					break;
				} else if (strResp.contains("没有查询到相关违法记录")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				} else {

					ret = "";
					Thread.sleep(100);
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
