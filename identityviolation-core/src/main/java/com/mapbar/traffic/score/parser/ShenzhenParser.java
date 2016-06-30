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

public class ShenzhenParser implements DriverParser {

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String ret = "";
		try {
			// Document doc = Jsoup.parse(strMsg);
			strMsg = "<table>" + strMsg + "</table>";
			Document doc = Jsoup.parseBodyFragment(strMsg);
			Element tr = doc.getElementsByTag("tr").get(0);
			if (tr == null) {
				return "";
			}
			Elements tds = tr.getElementsByTag("td");
			DriverCase dc = new DriverCase();
			JSONObject jobj = new JSONObject();
			jobj.put("driverlicense", tds.get(0).text().trim());// 驾驶证号
			String score = tds.get(2).text().trim();
			score = score.substring(0, score.length() - 1);
			jobj.put("score", score);// 扣分
			jobj.put("driverstate", tds.get(4).text().trim());// 驾驶证状态
			jobj.put("tjrq", tds.get(1).text().trim());// 审验日期
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
