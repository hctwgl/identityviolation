package com.mapbar.traffic.score.transfer;

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
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.GansuParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.FileUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.PropertiesUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class GansuService implements Transfer {

	private static final String GET_VIOLATION_URL = "http://wg.gsgajt.gov.cn:9080/GajtQuery/queryWfxw.queryInfo?method=getDriverInfo";
	private static final String STRING_REFERER = "http://wg.gsgajt.gov.cn:9080/GajtQuery/queryinfo/queryDriver_main.jsp";
	private static final String TO_YZM_PAGE = "http://wg.gsgajt.gov.cn:9080/GajtQuery/queryWfxw.queryInfo?method=toYzmPage";
	private static final String VALIDATE_IMAGE_URL = "http://wg.gsgajt.gov.cn:9080/GajtQuery/queryWfxw.queryInfo?method=getZp&zpcc=600&zpgg=340";
	private static final String IMAGE_TYPE = "png";
	GansuParser parser = new GansuParser();

	@Override
	public String checkDriverScore(DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (driverProfile != null && StringUtil.isNotEmpty(driverProfile.getCityPy())) {
				strResult = lookupScore(driverProfile, null);
				// strResult = lookupViolation(car, ProxyManager.next(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupScore(DriverProfile driverProfile, HttpHost next) {
		String ret = "";
		List<String> cookies = new ArrayList<String>();
		int Loop = 0;
		while (Loop < 3) {
			Loop++;
			try {
				String filePath = FileUtils.checkDerictor(PropertiesUtils.getProValue("IMAGE_STORE_PATH"), driverProfile.getCityPy(), String.valueOf(DateUtils.getCurrentDate()));
				// 得到图片
				String codeFileP = filePath + "/" + UUID.randomUUID();
				String validataCodeImage = codeFileP + "." + IMAGE_TYPE;
				HttpClientUtil.getUrlImageWithCookies(VALIDATE_IMAGE_URL, cookies, TO_YZM_PAGE, next, validataCodeImage, driverProfile.getCityPy());

				String strResp = "";
				// sfzmhm=622927198911200517&xm=%CD%F5%D2%BB%D4%AA&dabht=62&dabhbody=2900308491&dabh=622900308491&yzda=&button=%B2%E9+%D1%AF
				Map<String, String> params = new HashMap<String, String>();
				params.put("sfzmhm", driverProfile.getDriverLicense());
				params.put("xm", driverProfile.getDriverName());
				params.put("dabht", "62");
				params.put("dabhbody", driverProfile.getLssueArchive().substring(2));
				params.put("dabh", driverProfile.getLssueArchive());
				params.put("yzda", "");
				params.put("button", "查 询");
				strResp = HttpClientUtil.postURLContentsWithCookies(GET_VIOLATION_URL, params, cookies, STRING_REFERER, "GBK", next);
				// System.out.println(strResp);

				if (StringUtil.isNotEmpty(strResp) && strResp.contains("您输入的驾驶证信息有误，请检查输入")) {
					LogUtil.doMkLogData_JGU_With_Msg(driverProfile, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号、姓名或档案编号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("审验日期")) {
					JSONObject jobj = new JSONObject();
					parser.parse(strResp, jobj, driverProfile);
					DriverCase vc = new DriverCase();
					if (!jobj.isEmpty()) {
						vc.json.put("data", jobj);
						vc.json.put("status", "ok");
						ret = vc.toString();
					} else {
						ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}
					LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "ok", "");
					break;
				}

			} catch (SocketTimeoutException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "网络连接超时");
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "其他错误");
				e.printStackTrace();
			}
		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "其他错误");
		}
		return ret;
	}
}
