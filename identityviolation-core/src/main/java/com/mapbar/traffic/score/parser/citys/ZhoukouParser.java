package com.mapbar.traffic.score.parser.citys;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.DateUtils;

public class ZhoukouParser implements DriverParser {

	public String parse(String msg, DriverProfile driverProfile) {
		String strResult = "";
		try {
			// bgcolor="#999999"
			DriverCase vc = new DriverCase();
			JSONObject jobj = null;
			Document doc = Jsoup.parseBodyFragment(msg);
			Elements tables = doc.getElementsByTag("table");
			if (tables.size() > 0) {
				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				Elements spans = table.getElementsByTag("tr");
				if (trs.size() > 1) {
					jobj = new JSONObject();
					jobj.put("drivername", spans.get(0).text());
					jobj.put("driverlicense", spans.get(1).text());
					jobj.put("driverstate", spans.get(2).text());
					jobj.put("score", spans.get(3).text());
					jobj.put("tjrq", spans.get(4).text());
					jobj.put("qinfensj", DateUtils.qingFensj(jobj));
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
