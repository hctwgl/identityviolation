package com.mapbar.traffic.score.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.parser.base.PageParser;
import com.mapbar.traffic.score.utils.DateUtils;

public class HebeiParser implements PageParser {

	public void parse(String msg, JSONObject jobj, DriverProfile driverProfile) {
		try {
			Document doc = Jsoup.parse(msg);
			Elements tables = doc.getElementsByTag("table");
			Element table = tables.get(1);
			Elements trs = table.getElementsByTag("tr");
			Elements th = null;
			Elements td = null;
			if (tables.size() > 0) {
				if (trs.size() > 0) {
					for (Element tr : trs) {
						th = tr.getElementsByTag("th");
						td = tr.getElementsByTag("td");
						if ("档案编号".equals(th.text().trim())) {
							jobj.put("lissuearchive", driverProfile.getLssueArchive());
						} else if ("领证日期".equals(th.text().trim())) {
							jobj.put("lissuedate", td.text().trim());
						} else if ("准驾车型".equals(th.text().trim())) {
							jobj.put("carType", td.get(0).ownText().trim());
						} else if ("审验日期".equals(th.text().trim())) {
							jobj.put("tjrq", td.text().trim());
						} else if ("证件状态".equals(th.text().trim())) {
							jobj.put("driverstate", td.text().trim());
						} else if ("累计记分".equals(th.text().trim())) {
							jobj.put("score", td.text().trim());
						}
					}
				}
			}
			jobj.put("driverlicense", driverProfile.getDriverLicense());
			jobj.put("qinfensj", DateUtils.qingFensj(jobj));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
