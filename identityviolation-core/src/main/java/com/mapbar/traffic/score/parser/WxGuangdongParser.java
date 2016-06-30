package com.mapbar.traffic.score.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.DateUtils;

public class WxGuangdongParser implements DriverParser {
	/*
	 * <body> <main style="padding: 0" class="container-fluid"> <header class="title"> 查询到以下驾驶证信息 </header> <section style="background: white; padding-top: 0.5em"> <div class="row"> <label class="col-xs-5">姓名</label> <div class="col-xs-7"> <label id="r1" class="right"> 刘德 </label> </div> </div> <div class="row"> <label class="col-xs-5">本年度累计记分</label> <div class="col-xs-7"> <label id="r1" style="color: red; margin-top: 0px;" class="right">
	 * 0 </label> </div> </div> <div class="row"> <label class="col-xs-5">状态</label> <div class="col-xs-7"> <label id="r2" class="right"> 正常 </label> </div> </div> <div class="row"> <label class="col-xs-5">驾驶证类型</label> <div class="col-xs-7"> <label id="r2" class="right"> B2 </label> </div> </div> <div class="row"> <label class="col-xs-5">初始领证日期</label> <div class="col-xs-7"> <label id="r3" class="right"> 2007-09-11 </label> </div> </div>
	 * <div class="row"> <label class="col-xs-5">审验有效期止</label> <div class="col-xs-7"> <label id="r3" class="right"> 2023-09-11 </label> </div> </div> </section> </main> </body>
	 */
	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {
		String ret = "";
		try {
			Document doc = Jsoup.parse(strMsg);
			Element section = doc.getElementsByTag("section").get(0);
			if (section == null) {
				return "";
			}
			Elements divs = section.getElementsByTag("div");
			DriverCase dc = new DriverCase();
			JSONObject jobj = new JSONObject();
			String label = "";
			jobj.put("driverlicense", driverProfile.getDriverLicense());// 驾驶证号
			jobj.put("lissuearchive", driverProfile.getLssueArchive());// 驾驶证档案编号
			for (Element div : divs) {
				label = div.getElementsByTag("label").get(0).text().trim();
				if ("姓名".equals(label)) {
					jobj.put("drivername", div.getElementsByTag("div").get(1).getElementsByTag("label").text().trim());// 姓名
				} else if ("本年度累计记分".equals(label)) {
					jobj.put("score", div.getElementsByTag("div").get(1).getElementsByTag("label").text().trim());// 扣分
				} else if ("状态".equals(label)) {
					jobj.put("driverstate", div.getElementsByTag("div").get(1).getElementsByTag("label").text().trim());// 驾驶证状态
				} else if ("驾驶证类型".equals(label)) {
					jobj.put("carType", div.getElementsByTag("div").get(1).getElementsByTag("label").text().trim());// 准驾车型
				} else if ("初始领证日期".equals(label)) {
					jobj.put("lissuedate", div.getElementsByTag("div").get(1).getElementsByTag("label").text().trim());// 初次领证日期
				} else if ("审验有效期止".equals(label)) {
					jobj.put("tjrq", div.getElementsByTag("div").get(1).getElementsByTag("label").text().trim());// 审验日期
				}
			}
			jobj.put("qinfensj", DateUtils.qingFensj(jobj));
			if (!jobj.isEmpty()) {
				dc.json.put("data", jobj);
				dc.json.put("status", "ok");
				ret = dc.toString();
			} else {
				ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

}
