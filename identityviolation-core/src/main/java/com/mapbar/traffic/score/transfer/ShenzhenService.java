package com.mapbar.traffic.score.transfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.ShenzhenImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.ShenzhenParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class ShenzhenService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.stc.gov.cn:8082/szwsjj_web/ImgServlet.action?rnd=";
	private static final String GET_VIOLATION_URL = "http://www.stc.gov.cn:8082/szwsjj_web/jsyxx.action/firstQuery";
	private static final String STRING_REFERER = "http://www.stc.gov.cn:8082/szwsjj_web/jsp/xxcx/jsyxxcx.jsp";
	private static final String IMAGE_TYPE = "jpg";

	@Override
	public String checkDriverScore(DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (driverProfile != null && StringUtil.isNotEmpty(driverProfile.getCityPy())) {
				strResult = lookupScore(driverProfile, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	private String lookupScore(DriverProfile driverProfile, HttpHost next) {
		String strResp = null;

		List<String> vCookies = new ArrayList<String>();

		int Loop = 0;

		while (Loop < 18) {
			Loop++;
			try {
				String valiDateImageUrl = VALIDATE_IMAGE_URL + Math.random();
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), driverProfile.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, driverProfile.getCityPy());

				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}

				ShenzhenImageFilter imageFilter = new ShenzhenImageFilter();
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);

				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					code = code.toUpperCase();
					code = code.replace("}", "J");
					code = code.replace("£", "F");
					code = code.replace("?", "P");
					code = code.replaceAll("[^0-9a-zA-Z]", "");
					// System.out.println(" ShenzhenService  validataCode=========" + code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					// System.out.println(" ShenzhenService 验证码识别失败   validataCode=========" + code);
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}
				// SFZH=441426197909080031&cxlxmc=jsytjxxcx&yanZhen=2mk4
				// String postDate = "SFZH=" + driverProfile.getDriverLicense() + "&cxlxmc=jsytjxxcx&yanZhen=" + code;
				Map<String, String> params = new HashMap<String, String>();
				params.put("SFZH", driverProfile.getDriverLicense());
				params.put("cxlxmc", "jsytjxxcx");
				params.put("yanZhen", code);
				// System.out.println("postDate=========" + postDate);
				strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println("strResp=========" + strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("未查询到数据")) {
					strResp = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					return strResp;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("tr") && !strResp.contains("您的操作太过频繁") && !strResp.contains("该系统正在升级")) {
					ShenzhenParser parse = new ShenzhenParser();
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					strResp = parse.parse(strResp, driverProfile);
					break;
				} else {
					if (strResp.contains("该系统正在升级")) {
						LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "交管局故障");
						Thread.sleep(500);
						strResp = "";
						continue;
					}
					if (strResp.equals("2") || strResp.contains("您的操作太过频繁")) {
						Thread.sleep(500);
						strResp = "";
						LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "访问次数超限");
						continue;
					}
				}
			} catch (SocketTimeoutException e) {
				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接超时");
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "其他错误");
				e.printStackTrace();
			}
		}

		if ("".equals(strResp)) {
			LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "验证码识别失败");
		}
		return strResp;
	}

}
