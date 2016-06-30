package com.mapbar.provincetodo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.ChongqingImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.ChongqingParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class ChongqingService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://219.153.5.18:2333/cxxt/captcha-image.html?";

	private static final String GET_VIOLATION_URL = "http://219.153.5.18:2333/cxxt/jdccxjg.html";
	// http://www.cqjg.gov.cn/cxxt/jdcwf.html?sf=%25u6E1D&hphm=B2T175&vin=103453
	// http://219.153.5.18:2333/cxxt/jdcwf.html?sf=%25u6E1D
	private static final String STRING_REFERER = "http://219.153.5.18:2333/cxxt/jdcwf.html?sf=%25u6E1D";

	private static final String IMAGE_TYPE = "jpg";

	ChongqingParser parser = new ChongqingParser();

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

		Vector<String> vCookies = new Vector<String>();
		String btmId = "";
		if (btmId.length() > 6) {
			btmId = btmId.substring(btmId.length() - 6, btmId.length());
		} else if (btmId.length() < 6) {
			return null;
		}
		String referUrl = STRING_REFERER + "&hphm=" + "" + "&vin=" + btmId;
		// System.out.println(referUrl);
		Random random2 = new Random(100);

		int Loop = 0;
		while (Loop < 15) {
			Loop++;

			try {
				String valiDateImageUrl = VALIDATE_IMAGE_URL + random2.nextInt(100);
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, referUrl, next, validataCodeImage, car.getCityPy());
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;

				File ff = new File(validataCodeImage);
				if (!ff.exists()) {

					continue;
				}
				ChongqingImageFilter imageFilter = new ChongqingImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {

					continue;
				}
				String validataCode = "";
				try {
					validataCode = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);

					validataCode = validataCode.toLowerCase();
					validataCode = validataCode.replaceAll("[^0-9a-zA-Z]", "");

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(validataCode) || validataCode.length() < 5) {
					// System.out.println("ChongqingService 验证码识别失败   validataCode=========" + validataCode);

					// Thread.sleep(500);
					continue;
				} else if (validataCode.length() > 5) {
					validataCode = validataCode.substring(0, 5);
				}

				//
				//String postDate = "hpzl=" + "" + "&hphm=%25u6E1D" + "" + "&vin=" + btmId + "&yzm=" + validataCode;
				String strRep = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "UTF-8", next);

				// result1
				// <h3>【渝B2T175】涉及以下违法处理未完结 [非现场未处理]</h3><!-- Table --><div class='table'><table width='100%' border='0' cellspacing='0' cellpadding='0'><tr><th style='width: 90px'>号牌种类</th><th style='width: 80px'>违法时间</th><th>违法地点</th><th style='width:70px'>拟罚款金额</th><th style='width: 60px'>违法记分</th><th style='width: 70px'>违法当事人</th><th style='width: 60px'>违法行为</th></tr><tr><td> <img src='images/wcl.gif' title='非现场未处理'>
				// 小型汽车</td><td>2014-11-07</td><td>106省道珞璜路段执勤岗路段</td><td>200(元)</td><td>6</td><td>---</td><td><a onclick='javascript:viewDetail("小型汽车", "渝B2T175", "2014.11.07 10:06", "106省道珞璜路段执勤岗路段","重庆市江津区公安局交通巡逻警察支队珞璜平台","6","200(元)", "驾驶中型以上载客载货汽车、校车、危险物品运输车辆以外的其他机动车行驶超过规定时速20%以上未达到50%的", "1");' class='ico edit' name='ck1'>查看</a></td></tr></table><!-- Pagging --><div class='pagging'><div class='right'><span
				// class='paggingNormal'>共【1】条非现场未处理违法记录，累计罚款金额【200】元，累计违法记分【6】分</span></div></div></div><!--Table-->
				if (strRep.contains("涉及以下违法处理未完结")) {
					ret = parser.parse(strRep, car);
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				} else if (strRep.equals("5")) {

					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				} else if (strRep.equals("1")) {
					// System.out.println("验证码识别失败   ");
					Thread.sleep(500);
					continue;
				} else if (strRep.equals("3")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
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
