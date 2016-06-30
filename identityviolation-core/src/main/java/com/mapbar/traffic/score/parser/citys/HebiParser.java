package com.mapbar.traffic.score.parser.citys;

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

public class HebiParser implements DriverParser {
	public String parse(String result, DriverProfile driverProfile) {
		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
		try {
			DriverCase vc = new DriverCase();
			JSONObject jobj = null;
			Document doc = Jsoup.parseBodyFragment(result);
			Elements tables = doc.getElementsByAttributeValue("bgcolor", "#000000");
			if (tables.size() > 0) {
				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				if (trs.size() > 1) {
					trs.remove(0);
					trs.remove(0);
					Element tr = trs.get(0);
					String classc = tr.attr("class");
					if ("f12".equals(classc)) {
						Elements tds = tr.getElementsByTag("td");
						jobj = new JSONObject();
						String drivername = tds.get(0).getElementsByTag("div").get(0).text();
						String score = tds.get(3).getElementsByTag("div").get(0).text();
						String tjrq = tds.get(4).getElementsByTag("div").get(0).text();
						jobj.put("drivername", drivername);
						jobj.put("score", score);
						jobj.put("tjrq", tjrq);
						jobj.put("driverlicense", driverProfile.getDriverLicense());
						jobj.put("lissuearchive", driverProfile.getLssueArchive());
						jobj.put("qinfensj", DateUtils.qingFensj(jobj));
					}
					if (!jobj.isEmpty()) {
						vc.json.put("data", jobj);
						vc.json.put("status", "ok");
						strResult = vc.toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}
}
