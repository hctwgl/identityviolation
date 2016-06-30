package com.mapbar.provincetodo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.XiAnImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.XiAnParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class XiAnService implements Transfer {
	// http://www.gzjd.gov.cn:8008/cgs/captcha/getCaptcha.htm?type=2&d=1417069983578
	private static final String VALIDATE_IMAGE_URL = "http://117.36.53.122:9081/wfcx/imageServlet?";
	// http://www.cdjg.gov.cn/WebService/OnlineWork/QueryDrvOrVeh/QueryVehDetail.aspx
	private static final String GET_VIOLATION_URL = "http://117.36.53.122:9081/wfcx/query.do?";
	// http://117.36.53.122:9081/wfcx/query.do?actiontype=vioSurveil&code=ca02a40224106fc33424a0ce01f09b74&hpzl=02&hphm=A0H202&tj=FDJH&tj_val=66495110528826&jdccode=MRWQ
	private static final String STRING_REFERER = "http://www.xianjj.com/jjwscgs.htm";

	// http://117.36.53.122:9081/wfcx/query.do?actiontype=vioSurveil&code=ac5de2513d8cedd2265722614bcad71d&hpzl=02&hphm=A0H202&tj=FDJH&tj_val=66495110528826&jdccode=K1H8
	// http://117.36.53.122:9081/wfcx/query.do?actiontype=vioSurveil&code=ac5de2513d8cedd2265722614bcad71d&hpzl=02&hphm=A76B22&tj=FDJH&tj_val=C118472&jdccode=XGJC
	private static final String IMAGE_TYPE = "jpg";

	XiAnParser parse = new XiAnParser();

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

		Vector<String> vCookies = new Vector<String>();
		//String code = MD5Util.MD5(DateUtils.getCurrentDateStr());
		int Loop = 0;
		while (Loop < 10) {
			Loop++;
			try {
				String valiDateImageUrl = VALIDATE_IMAGE_URL + Math.random();
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				XiAnImageFilter imageFilter = new XiAnImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {

					continue;
				}
				String validataCode = "";
				try {
					validataCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);

					validataCode = validataCode.replaceAll("\\|", "I");
					validataCode = validataCode.toUpperCase();
					validataCode = validataCode.replaceAll("[^0-9a-zA-Z]", "");
					// System.out.println("XiAnService validataCode=========" + validataCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 4) {
					// System.out.println("XiAnService 验证码识别失败   validataCode=========" + validataCode);
					continue;
				} else if (validataCode.length() > 4) {
					validataCode = validataCode.substring(0, 4);
				}
				//String getData = "actiontype=vioSurveil&code=" + code.toLowerCase() + "&hpzl=" + car.getCityPy() + "&hphm=" + car.getCityPy() + "&tj=FDJH&tj_val=" + car.getCityPy() + "&jdccode=" + validataCode;

				String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "GBK", next);
				// .getURLContentsWithCookies(GET_VIOLATION_URL+getData, vCookies, STRING_REFERER, next);
				System.out.println("strRep===" + strRep);
				if (StringUtil.isNotEmpty(strRep) && (strRep.contains("发动机号匹配错误") || strRep.contains("号牌号码和号牌种类输入错误,请仔细核对"))) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,发动机号或车牌号错误！");
				} else if (StringUtil.isNotEmpty(strRep) && strRep.contains("违法未处理")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = parse.parse(strRep, car);
					break;
				} else if ((StringUtil.isNotEmpty(strRep) && strRep.contains("正常"))) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					break;

				} else if ((StringUtil.isNotEmpty(strRep) && strRep.contains("验证码出错"))) {
					// System.out.println("验证码出错");
					Thread.sleep(100);
					continue;
				} else {

					Thread.sleep(500);
					continue;
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
