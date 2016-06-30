package com.mapbar.traffic.score.transfer.citys;

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

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.image.orc.OCR;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.citys.DalianParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CityDalianService implements Transfer {

	private static final String VALIDATE_IMAGE_URL = "http://www.dalianjiaojing.com/images/image.jsp";
	private static final String GET_VIOLATION_URL = "http://www.dalianjiaojing.com/chaxun/driverSearch.htm";
	private static final String STRING_REFERER = "http://www.dalianjiaojing.com/chaxun/driverSearch.htm";
	private static final String IMAGE_TYPE = "jpg";
	DalianParser parser = new DalianParser();

	@Override
	public String checkDriverScore(DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (driverProfile != null && StringUtil.isNotEmpty(driverProfile.getCityPy())) {
				// strResult = lookupViolation(car,ProxyManager.next(true));
				strResult = lookupScore(driverProfile, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupScore(DriverProfile driverProfile, HttpHost next) {
		String ret = "";

		int Loop = 0;
		while (Loop < 8) {
			Loop++;
			List<String> cookies = new ArrayList<String>();
			try {
				// Map<String, String> hiddenMap = parser.parseHidden(indexStr,"UTF-8");
				String valiDateImageUrl = VALIDATE_IMAGE_URL;
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), driverProfile.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));
				// System.out.println(filePath);

				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				// 得到图片
				HttpClientUtil.getUrlImageWithCookies(valiDateImageUrl, cookies, STRING_REFERER, next, validataCodeImage, driverProfile.getCityPy());

				File valiDataCodeF = new File(validataCodeImage);

				if (!valiDataCodeF.exists()) {
					continue;
				}
				String code = "";
				try {
					code = new OCR().recognizeText(valiDataCodeF, IMAGE_TYPE, codeFileP);
					code = code.toUpperCase();
					code = code.replaceAll("[^0-9a-zA-Z]", "");
					// System.out.println("CityDalianService validataCode=========" + code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!StringUtil.isNotEmpty(code) || code.length() < 4) {
					// System.out.println("CityDalianService 验证码识别失败   validataCode=========" + code);
					continue;
				} else if (code.length() > 4) {
					code = code.toUpperCase().substring(0, 4);
				}

				// m=jifen&jszh=210222198206244727&dsr=210211365167&yanzhengma=5856&x=24&y=17
				// m=jifen&jszh=210222198206244727&dsr=210211365167&yanzhengma=2422&x=31&y=30
				// String postData = "m=jifen&jszh=" + driverProfile.getDriverLicense() + "&dsr=" + driverProfile.getLssueArchive() + "&yanzhengma=" + code + "&x=24&y=17";
				// System.out.println(postData);
				Map<String, String> params = new HashMap<String, String>();
				params.put("m", "jifen");
				params.put("jszh", driverProfile.getDriverLicense());
				params.put("dsr", driverProfile.getLssueArchive());
				params.put("yanzhengma", code);
				params.put("x", "24");
				params.put("y", "17");
				String strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, cookies, STRING_REFERER, "gbk", next);
				//System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && !strResp.contains("驾驶人姓名")) {
					continue;
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("该驾驶人暂无记录")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("驾驶人信息错误,驾驶证号或档案编号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && !strResp.contains("驾驶人姓名")) {
					continue;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("查询结果")) {
					JSONObject jobj = new JSONObject();
					parser.parse(strResp, jobj, driverProfile);
					DriverCase vc = new DriverCase();
					if (jobj.size() > 0) {
						vc.json.put("data", jobj);
						vc.json.put("status", "ok");
						ret = vc.toString();
					} else {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "ok", "");
					break;

				} else {
					Thread.sleep(500);
					continue;
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

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "验证码识别失败");
		}
		return ret;
	}
}
