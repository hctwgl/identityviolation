package com.mapbar.provincetodo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.Vector;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.WxShandongParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class WxShandongService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://m.sdjtaq.cn/index.php?r=vio/captcha&vq=";

	private static final String GET_VIOLATION_URL = "http://m.sdjtaq.cn/index.php?r=Vio/vio&src=wzcx";
	private static final String STRING_REFERER = "http://m.sdjtaq.cn/index.php?r=vio/vio&src=wzcx";
	private static final String IMAGE_TYPE = "png";

	WxShandongParser parser = new WxShandongParser();

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

		Vector<String> vCookies = new Vector<String>();

		int Loop = 0;

		String btmId = car.getCityPy();
		if (btmId.length() > 6) {
			btmId = btmId.substring(btmId.length() - 6, btmId.length());
		} else if (btmId.length() < 6) {
			// 查询规则变化，请输入xxxxxx
			return ResultCache.toErrJsonResult("查询规则变化，请输入车架号后六位，给您带来的不变，请您谅解！");
		}

		while (Loop < 20) {
			Loop++;
			try {
				String valiDateImageUrl = VALIDATE_IMAGE_URL + Math.random();
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpsUtils.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());

				File ff = new File(validataCodeImage);
				if (!ff.exists()) {

					continue;
				}

				String code = "";
				try {
					code = new OCR().recognizeText(ff, IMAGE_TYPE, codeFileP);
					// System.out.println("validataCode=========" + code);
					code = code.replaceAll("[^0-9]", "");
					// System.out.println(" ShandongService validataCode=========" + code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					// System.out.println(" ShandongService 验证码识别失败   validataCode=========" + code);
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}

				// hphm=%E9%B2%81AWU088&sbdm=007268&hpzl=02&code=6238
				String postDate = "hpzl=" + car.getCityPy() + "&hphm=" + URLEncoder.encode(car.getCityPy(), "UTF-8") + URLEncoder.encode(car.getCityPy(), "UTF-8") + "&sbdm=" + btmId + "&code=" + code;
				// System.out.println("postDate===="+postDate);
				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println("strResp======"+strResp);

				if (StringUtil.isNotEmpty(strResp) && strResp.contains("验证码错误")) {
					// System.out.println("验证码错误======");
					continue;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("车辆识别代码错误")) {
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号错误！");
				}

				else if (StringUtil.isNotEmpty(strResp) && strResp.contains(",扣分,罚款")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					break;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("class=\"wz_xinxi\"")) {
					ret = parser.parse(strResp, car);
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
}
