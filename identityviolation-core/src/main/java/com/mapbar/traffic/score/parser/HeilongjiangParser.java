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

public class HeilongjiangParser implements DriverParser {

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {
		String result = "";
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			strMsg = "<table>" + strMsg + "</table>";
			Document doc = Jsoup.parse(strMsg);
			Elements trs = doc.getElementsByTag("tr");

			if (trs != null) {
				Element tr = trs.get(1);
				DriverCase vc = new DriverCase();
				JSONObject jobj = null;
				Elements tds = tr.getElementsByTag("td");
				if (tds != null && tds.size() > 0) {
					jobj = new JSONObject();
					jobj.put("driverlicense", tds.get(0).text());
					jobj.put("drivername", tds.get(1).text());
					jobj.put("score", tds.get(2).text());
					jobj.put("tjrq", tds.get(3).text().substring(0, 10));
					jobj.put("lissuearchive", driverProfile.getLssueArchive());
					jobj.put("qinfensj", DateUtils.qingFensj(jobj));
				}
				if (!jobj.isEmpty()) {
					vc.json.put("data", jobj);
					vc.json.put("status", "ok");
					result = vc.toString();
				} else {
					result = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
}
