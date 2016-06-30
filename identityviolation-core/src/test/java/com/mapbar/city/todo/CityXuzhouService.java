package com.mapbar.city.todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;

import com.alibaba.fastjson.JSONArray;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.XuzhouImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.XuzhouParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityXuzhouService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://www.xzjgw.com/2010cx/manage/code.php";

	private static final String GET_VIOLATION_URL = "http://www.xzjgw.com/2010cx/cars.php";

	//private static final String CHECK_CODE = "http://www.xzjgw.com/2010cx/ajax.check.wzjdc.php";
	private static final String STRING_REFERER = "http://www.xzjgw.com/2010cx/cars.php";

	private static final String IMAGE_TYPE = "gif";
	XuzhouParser parser = new XuzhouParser();

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
		while (Loop < 3) {
			Loop++;

			try {

				Vector<String> vCookies = new Vector<String>();
				String valiDateImageUrl = VALIDATE_IMAGE_URL;
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
				XuzhouImageFilter imageFilter = new XuzhouImageFilter();
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);

				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					// System.out.println("validataCode=========" + code);

					code = code.replace("~", "—");
					code = code.replace("A", "—");
					if (!code.contains("+") && !code.contains("—") && !code.contains("-")) {
						continue;
					}
					code = code.replace("o", "0");
					code = code.replace("O", "0");
					code = code.replace("S", "8");
					code = code.replace("s", "8");
					code = code.replaceAll("[^0-9+—-]", "");
					if (code.contains("+")) {
					}
					if (code.contains("—")) {
					}
					if (code.contains("-")) {
					}
					// System.out.println("validataCode=========" + code);
					// System.out.println("num=========" + num);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// LCNNM=小型汽车&shengfen=苏D&chepai=J097W&fadongji=62754W&vcode=8174&Submit=查 询
				//
				//String postData = "isShow=1&storage=1&wzdz=&charset=1&hphm=" + URLEncoder.encode(car.getCityPy(), "utf-8") + car.getCityPy() + "&hpzl=2&fdjh=" + engId + "&code=" + num + "&param1=&param2=";
				// System.out.println("postData===="+postData);
				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, null, vCookies, STRING_REFERER, "utf-8", next);

				System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.startsWith("<script>alert('")) {
					continue;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("本地违章信息") && strResp.contains("异地违章信息")) {
					JSONArray jcases = new JSONArray();

					Pattern p = Pattern.compile("data:\\s\\{\"Rows\":(.*?),\"Total\":");
					Matcher match = p.matcher(strResp);

					if (match.find()) {
						String jsonData = match.group(1);
						if (StringUtil.isNotEmpty(jsonData) && !"[]".equals(jsonData)) {
							parser.parse(jsonData, jcases);
						}
					}
					if (match.find()) {
						String jsonData = match.group(1);
						if (StringUtil.isNotEmpty(jsonData) && !"[]".equals(jsonData)) {
							parser.parse(jsonData, jcases);
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
