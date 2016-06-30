package com.mapbar.traffic.score.transfer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.ShanxiParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.ProxyManager;
import com.mapbar.traffic.score.utils.StringUtil;

public class ShanxiService implements Transfer {

	private static final String GET_VIOLATION_URL = "http://59.49.18.116:8008/sxwwpt_wai/inquire/infoQueryAction!motoristInquire.action";
	private static final String GET_VIOLATION_URL1 = "http://59.49.18.116:8008/sxwwpt_wai/inquire/infoQueryAction!integralQuery.action";// 为获得驾驶人姓名
	private static final String STRING_REFERER = "http://59.49.18.116:8008/sxwwpt_wai/jsp/inquires/motoristInquirec.jsp";
	private static final String STRING_REFERER1 = "http://59.49.18.116:8008/sxwwpt_wai/jsp/inquires/coringQueryFrmc.jsp";
	// private static final String IMAGE_TYPE="jpg";
	ShanxiParser parser = new ShanxiParser();

	@Override
	public String checkDriverScore(DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (driverProfile != null && StringUtil.isNotEmpty(driverProfile.getCityPy())) {
				strResult = lookupScore(driverProfile, ProxyManager.next(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupScore(DriverProfile driverProfile, HttpHost next) {
		String ret = "";
		int Loop = 0;
		while (Loop < 3) {
			Loop++;
			try {
				List<String> cookies = new ArrayList<String>();
				// String imageUrl = VALIDATE_IMAGE_URL+new Date().getTime();
				// NetUtil.getURLContentsWithCookies(imageUrl, vCookies, STRING_REFERER, next);
				// String code = NetUtil.postURLContentsWithCookies(GET_VIOLATION_CODE, "authCode=vehillegal2",vCookies, STRING_REFERER, "UTF-8",next);
				Random random = new Random(9);
				String code = random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9);
				// ?type=1&dabh=140100674335&jszh=140121198211200016&authCode=9478&authCode2=9478
				// ?type=1&dabh=140100674335&jszh=140121198211200016&authCode=6455&authCode2=6455 为获得驾驶人姓名
				String getDate = "?type=1&dabh=" + driverProfile.getLssueArchive() + "&jszh=" + driverProfile.getDriverLicense() + "&authCode=" + code + "&authCode2=" + code;
				String getDate1 = "?type=1&dabh=" + driverProfile.getLssueArchive() + "&jszh=" + driverProfile.getDriverLicense() + "&authCode=" + code + "&authCode2=" + code;
				// System.out.println(getDate);
				String strResp = HttpClientUtil.getURLContentsWithCookies(GET_VIOLATION_URL + getDate, cookies, STRING_REFERER, next);
				String strResp1 = HttpClientUtil.getURLContentsWithCookies(GET_VIOLATION_URL1 + getDate1, cookies, STRING_REFERER1, next);

				// System.out.println(strResp);
				// System.out.println(strResp1);

				if (StringUtil.isNotEmpty(strResp) && strResp.contains("没有查询到相关数据")) {
					LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "信息输入错误");
					return ResultCache.toErrJsonResult("驾驶证信息错误,驾驶证号或档案编号号错误！");
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("class=\"if_tr\"")) {
					JSONObject jobj = new JSONObject();
					if (strResp1.contains("姓名")) {
						parseName(strResp1, jobj, driverProfile);
					}
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
				} else {
					Thread.sleep(1000);
					continue;
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
			LogUtil.doMkLogData_JGUProv_With_Msg(driverProfile, "err", "验证码识别失败");
		}
		return ret;
	}

	public void parseName(String strResult, JSONObject jobj, DriverProfile driverProfile) {
		try {
			Document doc = Jsoup.parse(strResult);
			Elements trs = doc.getElementsByClass("if_tr");
			if (trs == null) {
				return;
			}
			if (trs != null) {
				for (int i = 0; i < trs.size(); i++) {
					Element tr = trs.get(i);
					Elements tds = tr.getElementsByTag("td");
					String drivername = tds.get(1).text();
					driverProfile.setDriverName(drivername);
					jobj.put("drivername", driverProfile.getDriverName());// 姓名
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
