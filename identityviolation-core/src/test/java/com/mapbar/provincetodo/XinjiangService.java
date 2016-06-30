package com.mapbar.provincetodo;

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
import com.mapbar.traffic.score.image.citys.XinjiangImageFilter;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.XinjiangParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class XinjiangService implements Transfer {
	//
	private static final String VALIDATE_IMAGE_URL = "http://124.117.209.131:8088/CheckCode.aspx?";
	// http://124.117.209.131:8088/WebVeh/WfQuery/JdcWfQuery/Search.aspx
	private static final String GET_VIOLATION_URL = "http://124.117.209.131:8088/WebVeh/WfQuery/JdcWfQuery/Search.aspx";
	private static final String STRING_REFERER = "http://124.117.209.131:8088/WebVeh/WfQuery/JdcWfQuery/Search.aspx";
	private static final String IMAGE_TYPE = "gif";

	XinjiangParser parser = new XinjiangParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, ProxyManager.next(true));
				// strResult = lookupViolation(car, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpHost next) {
		String ret = null;

		List<String> vCookies = new ArrayList<String>();
		String btmId = car.getCityPy();
		if (btmId.length() > 4) {
			btmId = btmId.substring(btmId.length() - 4, btmId.length());
		} else if (btmId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,车架号过短！");
		}

		int Loop = 0;

		while (Loop < 3) {
			Loop++;
			try {
				//String indexStr = HttpClientUtil.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "GBK");

				//Map<String, String> hiddenMap = parser.parseHidden(indexStr);

				String valiDateImageUrl = VALIDATE_IMAGE_URL + Math.random();
				// 校验验证码存储目录是否存在 不存在则创建
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), car.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片

				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, vCookies, STRING_REFERER, next, validataCodeImage, car.getCityPy());
				// System.out.println("vCookies========="+vCookies);
				File ff = new File(validataCodeImage);
				if (!ff.exists()) {
					continue;
				}
				XinjiangImageFilter imageFilter = new XinjiangImageFilter();
				String newImagePath = filePath + "/" + UUID.randomUUID() + "." + IMAGE_TYPE;
				imageFilter.dealImage(validataCodeImage, newImagePath, IMAGE_TYPE);

				File valiDataCodeF = new File(newImagePath);
				if (!valiDataCodeF.exists()) {

					continue;
				}

				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					// System.out.println("validataCode=========" + code);
					code = code.toUpperCase();

					code = code.replaceAll("[^0-9]", "");
					System.out.println("validataCode=========" + code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					System.out.println(" XinjiangService 验证码识别失败   validataCode=========" + code);
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}

				// ctl00%24Content%24ddlHpzl:02
				// ctl00%24Content%24ddlProvince:新
				// ctl00%24Content%24txtHphm:K98521
				// ctl00%24Content%24txtClsbdh:0784
				// ctl00%24Content%24txtYzm:3365
				// ctl00%24Content%24btnOK:提 交
				// &ctl00$Content$ddlHpzl=02&ctl00$Content$ddlProvince=新&ctl00$Content$txtHphm=K98521&ctl00$Content$txtClsbdh=0784&ctl00$Content$txtYzm=5366&ctl00$Content$btnOK=提 交
				/*
				 * String postDate = parser.getParamDate(hiddenMap)+"&ctl00$Content$ddlHpzl=02"+ "&ctl00$Content$ddlProvince="+URLEncoder.encode(car.getCityPy(), "GBK") +"&ctl00$Content$txtHphm="+car.getCityPy() +"&ctl00$Content$txtClsbdh="+btmId +"&ctl00$Content$txtYzm="+code +"&ctl00$Content$btnOK=%CC%E1++%BD%BB";
				 */
				Map<String, String> postDate = new HashMap<String, String>();
				System.out.println("postDate=========" + postDate);
				String strResp = HttpClientUtil.postURLContentsWithCookiesForXinjiang(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "GBK", next);
				System.out.println("strResp=========" + strResp);

				// 验证码失效！
				if (StringUtil.isNotEmpty(strResp) && (strResp.contains("验证码失效") && strResp.contains("验证码不正确"))) {
					// System.out.println(" HainanService 验证码识别失败   validataCode=========" + code);
					continue;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("未查询到违法信息")) {
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					LogUtil.doMkLogData_JGUProv_With_Msg(car, "ok", "");
					LogUtil.doMkLogData_jiaoguanProv(car, "ok");
					break;
				}
				if (StringUtil.isNotEmpty(strResp) && (strResp.contains("车辆识别代号有误") || strResp.contains("车辆信息不存在"))) {
					ret = ResultCache.toErrJsonResult("车辆信息错误,车架号或车牌号错误！");
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					break;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("class=\"wfzx_t2\"")) {
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
