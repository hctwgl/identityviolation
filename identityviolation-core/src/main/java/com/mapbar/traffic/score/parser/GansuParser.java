package com.mapbar.traffic.score.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.parser.base.PageParser;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class GansuParser  implements PageParser {

	public void parse(String result, JSONObject jobj, DriverProfile driverProfile) {
		try {
			Document doc = Jsoup.parseBodyFragment(result);
			Elements tables = doc.getElementsByAttributeValue("bgcolor", "#c5d7e8");
			if (tables.size() > 0) {
				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				Element tr = null;
				Elements tds = null;
				String start = "";
				String end = "";
				if (trs.size() > 0) {
					String nbsp = Jsoup.parse("&nbsp;").text();
					for (int i = 0; i < trs.size(); i++) {
						tr = trs.get(i);
						tds = tr.getElementsByTag("td");
						if (i == 0) {
							jobj.put("drivername", driverProfile.getDriverName());
							jobj.put("driverlicense", driverProfile.getDriverLicense());
							jobj.put("lissuearchive", driverProfile.getLssueArchive());
						} else if (i == 1) {
							jobj.put("tjrq", tds.get(1).text().replace(nbsp, ""));
						} else if (i == 2) {
							start = tds.get(5).text().replace(nbsp, "");
						} else if (i == 3) {
							end = tds.get(5).text().replace(nbsp, "");
						} else if (i == 4) {
							jobj.put("carType", tds.get(1).text().replace(nbsp, ""));
							jobj.put("score", tds.get(3).text().replace(nbsp, ""));
							jobj.put("driverstate", tds.get(5).text().replace(nbsp, ""));
						}
					}
				}
				if (StringUtil.isNotEmpty(start) && StringUtil.isNotEmpty(end)) {
					jobj.put("validity", "从" + start + "至" + end);
				}
				jobj.put("qinfensj", DateUtils.qingFensj(jobj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
