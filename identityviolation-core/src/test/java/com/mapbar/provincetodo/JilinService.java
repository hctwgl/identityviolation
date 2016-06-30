package com.mapbar.provincetodo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.jdesktop.swingx.util.OS;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.JilinParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class JilinService implements Transfer {
	// http://www.jljj.cn/thirdparty/cx/val.jsp?change=yes&random=0.3709979068953544
	// http://www.jljj.cn/thirdparty/cx/cgcx.jsp

	private static final String STRING_REFERER = "http://www.jljj.cn/";

	// http://www.jljj.cn/thirdparty/cx/val.jsp?change=yes&random=0.17771507054567337
	private static final String VALIDATE_IMAGE_URL = "http://www.jljj.cn/thirdparty/cx/val.jsp?change=yes&random=";
	private static final String GET_VIOLATION_URL = "http://www.jljj.cn/thirdparty/cx/cgcx.jsp";
	private static final String IMAGE_TYPE = "jpg";
	JilinParser parser = new JilinParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, null);
				// strResult = lookupViolation(car,ProxyManager.next(true));
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
		int Loop = 0;
		Vector<String> vCookies = new Vector<String>();
		while (Loop < 10) {
			Loop++;
			try {
				if (Loop == 1) {
				 HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "UTF-8");
				}
				System.out.println(vCookies);
				String imageUrl = VALIDATE_IMAGE_URL + Math.random();

				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));
				// System.out.println(filePath);

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// System.out.println(validataCodeImage);
				// 得到图片
				// System.out.println(imageUrl);
				HttpsUtils.getUrlImageWithCookies(imageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());

				File valiDataCodeF = new File(validataCodeImage);

				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP, getOcrCommend(valiDataCodeF, codeFileP));
					// System.out.println("JilinService validataCode=========" + code);
					code = code.replaceAll(" ", "");
					code = code.replace("i*l", "神");
					// System.out.println("JilinService validataCode0=========" + code);
					code = code.replace("|日", "旧");
					// System.out.println("JilinService validataCode1=========" + code);
					code = code.replace("\"昌", "唱");
					code = code.replace("H昌", "唱");
					// System.out.println("JilinService validataCode2=========" + code);
					code = code.replace("l日", "旧");
					code = code.replace("f晏", "慢");
					// System.out.println("JilinService validataCode3=========" + code);
					if (code.length() > 4) {
						code = code.replace("儡立", "位");
						code = code.replace("儡牛", "件");
						code = code.replace("置去", "法");
						code = code.replace("皿云", "际");
						code = code.replace("妻去", "法");
						code = code.replace("焘申", "神");
						code = code.replace("儡壬", "任");
						code = code.replace("素去", "法");
					}

					code = code.replaceAll("[^\u4E00-\u9FA5]", "");
					// System.out.println("JilinService validataCode_f=========" + code);

					// /code = code.replace("儡壬","任");
					// System.out.println("JilinService validataCode4=========" + code);
					// System.out.println("JilinService validataCode5=========" + code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() != 4) {
					System.out.println("JilinService 验证码识别失败   validataCode=========" + code);
					continue;
				}
				// System.out.println("JilinService validataCode=========" + code);
				// province:吉
				// hphm:AM331E
				// hpzl:02
				// engine:0624
				// yzm:座背压件
				String postData = "province=" + URLEncoder.encode(car.getCityPy(), "utf-8") + "&hphm=" + car.getCityPy() + "&engine=" + btmId + "&yzm=" + "&hpzl=" + car.getCityPy();

				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "UTF-8", next);
				System.out.println(strResp);

				if (StringUtil.isNotEmpty(strResp) && strResp.contains("车辆识别代号后四位输入有误")) {
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("验证码错误")) {

					System.out.println("JilinService 验证码识别失败   validataCode=========" + code);
					// Thread.sleep(500);
					continue;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("违法未处理")) {
					ret = parser.parse(strResp, car);
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					break;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("正常")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					break;
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

	private static List<String> getOcrCommend(File imageFile, String codeFilePath) {
		List<String> cmd = new ArrayList<String>();
		if (OS.isWindowsXP()) {
			cmd.add(OCR.tessPath + "tesseract");
		} else if (OS.isLinux()) {
			cmd.add("tesseract");
		} else {
			cmd.add(OCR.tessPath + "tesseract.exe");
		}
		cmd.add(imageFile.getPath());
		cmd.add(codeFilePath);
		cmd.add("-psm");
		cmd.add("6");
		cmd.add(OCR.LANG_OPTION);
		cmd.add("chi_sim");
		// System.out.println(cmd);
		return cmd;
	}
}
