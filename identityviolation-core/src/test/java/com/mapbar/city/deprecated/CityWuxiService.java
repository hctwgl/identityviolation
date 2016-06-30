package com.mapbar.city.deprecated;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpHost;

import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.citys.WuxiImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.WuxiParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

@Deprecated
public class CityWuxiService implements Transfer {
	private static final String VALIDATE_IMAGE_URL = "http://202.102.2.157/data/captcha/ckstr.php?";

	private static final String GET_VIOLATION_URL = "http://202.102.2.157/jxjapi.php?op=01";

	private static final String STRING_REFERER = "http://202.102.2.157/jxjapi.php?op=01";

	private static final String IMAGE_TYPE = "jpg";

	WuxiParser parser = new WuxiParser();

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
		try {

			String engId = car.getCityPy();
			if (engId.length() > 6) {
				engId = engId.substring(engId.length() - 6, engId.length());
			} else if (engId.length() < 6) {
				return ResultCache.toErrJsonResult("车辆信息错误,发动机号过短！");
			}

			int Loop = 0;
			while (Loop < 8) {
				Loop++;

				try {
					String refer = STRING_REFERER + "&hpzl=02&hm=" + car.getCityPy();
					List<String> vCookies = new ArrayList<String>();
					String valiDateImageUrl = VALIDATE_IMAGE_URL + new Date().getTime();
					String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

					String codeFileP = filePath + "/" + UUID.randomUUID();
					String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
					// 得到图片
					HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, refer, next, validataCodeImage, car.getCityPy());
					File ff = new File(validataCodeImage);
					if (!ff.exists()) {
						continue;
					}
					String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
					WuxiImageFilter imageFilter = new WuxiImageFilter();
					imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);
					// ImageIOHelper.createImage(new File(validataCodeImage), IMAGE_TYPE, newImagePath);
					File valiDataCodeF = new File(validataCodeImage);
					if (!valiDataCodeF.exists()) {
						continue;
					}
					String code = "";
					try {
						code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
						// System.out.println("validataCode=========" + code);
						code = code.toUpperCase();
						code = code.replace(" ", "");
						code = code.replace("}", "J");
						code = code.replaceAll("[^a-zA-Z]", "");
						// System.out.println("validataCode=========" + code);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
						continue;
						// System.out
						// .println("验证码识别失败   validataCode=========" + code);
					} else if (code.length() > 4) {
						code = code.toUpperCase().substring(0, 4);
					}
					// LCNNM=小型汽车&shengfen=苏D&chepai=J097W&fadongji=62754W&vcode=8174&Submit=查 询

					// String postData="fdjh="+engId+"&validate="+code+"&action=select&hm="+car.getCityPy()+"&hpzl=02";
					Map<String, String> postData = new HashMap<String, String>();
					// System.out.println("postData===="+postData);
					String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, postData, vCookies, STRING_REFERER, "utf-8", next);

					// System.out.println(strResp);

					if (StringUtil.isNotEmpty(strResp) && strResp.contains("请检查验证码是否正确")) {
						continue;
					} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("输入的车牌号或发动机信息不正确")) {
						return ResultCache.toErrJsonResult("车辆信息错误,车牌或发动机号错误！");
					} else if (strResp.contains("class=\"cls-qa-table\"") && strResp.contains("暂无数据")) {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						break;

					} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("class=\"cls-qa-table\"")) {
						ret = parser.parse(strResp, car);
						LogUtil.doMkLogData_jiaoguanju(car, "ok");
						break;
					}
					// String postData="LCNNM="+URLEncoder.encode("小型汽车", "GBK")+"&shengfen="
					// +URLEncoder.encode(car.strState, "GBK")+car.strRegId.substring(0, 1)+
					// "&chepai="+car.strRegId.substring(1)+"&fadongji="+engId+"&vcode="+code+"&Submit=%B2%E9++%D1%AF";
					//
					// String strResp = NetUtil.postURLContentsWithCookies(GET_VIOLATION_URL,
					// postData, vCookies, STRING_REFERER, "GBK", next);
					// System.out.println("strResp========="+strResp);
					// if(StringUtil.isValid(strResp) && strResp.contains("请输入正确的验证码") ){
					// continue;
					// }else if(StringUtil.isValid(strResp) && strResp.contains("没有未处理电子警察违法记录")){
					// ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					// LogHelp.doMkLogData_jiaoguanju(car, "ok");
					// break;
					// }
					// if(StringUtil.isValid(strResp) && strResp.contains("您输入的发动机号后六位") && strResp.contains("不匹配，请仔细核对") ){
					// return ResultCache.toErrJsonResult("车辆信息错误,发动机编号错误！");
					// }
					// if(StringUtil.isValid(strResp) && strResp.contains("条未处理电子警察违法记录")){
					// LogHelp.doMkLogData_jiaoguanju(car, "ok");
					// ret = parser.parse(strResp);
					// break;
					// }else{
					// break;
					// }
				} catch (Exception e) {
					LogUtil.doMkLogData_jiaoguanju(car, "err");
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// LogHelp.doMkLogData_jiaoguanju(car, "err");
			e.printStackTrace();
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_jiaoguanju(car, "err");
		}
		return ret;
	}
}
