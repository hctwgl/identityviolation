package com.mapbar.traffic.score.parser.citys;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.StringUtil;

public class DandongParser implements DriverParser {
	public String parse(String result, DriverProfile driverProfile) {
		String strResult = "";

		try {
			DriverCase vc = new DriverCase();
			JSONObject jobj = null;
			Document doc = Jsoup.parseBodyFragment(result);
			Elements tables = doc.getElementsByAttributeValue("bgcolor", "#CCCCCC");
			if (tables.size() > 0) {
				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				Element tr = null;
				Elements tds = null;
				if (trs.size() > 0) {
					jobj = new JSONObject();
					jobj.put("lissuearchive", driverProfile.getLssueArchive());
					String start = "";
					String end = "";
					for (int i = 0; i < trs.size(); i++) {
						tr = trs.get(i);
						tds = tr.getElementsByTag("td");
						for (int j = 0; j < tds.size(); j++) {
							if ("姓　　名".equals(tds.get(j).text())) {
								jobj.put("drivername", tds.get(j + 1).text());
							} else if ("身份证号".equals(tds.get(j).text())) {
								String text = tds.get(j + 1).text();
								if (StringUtils.isNotBlank(text)) {
									jobj.put("driverlicense", text);
								} else {
									jobj.put("driverlicense", driverProfile.getDriverLicense());
								}
							} else if ("清分日期".equals(tds.get(j).text())) {
								jobj.put("qinfensj", tds.get(j + 1).text());
							} else if ("有效期始".equals(tds.get(j).text())) {
								start = tds.get(j + 1).text();
							} else if ("有效期止".equals(tds.get(j).text())) {
								end = tds.get(j + 1).text();
							} else if ("累积记分".equals(tds.get(j).text())) {
								jobj.put("score", tds.get(j + 1).text());
							} else if ("状 态".equals(tds.get(j).text())) {
								jobj.put("driverstate", tds.get(j + 1).text());
							} else if ("审验有效期止（仅限准驾车型为：A1、A2、B1、B2）".equals(tds.get(j).text())) {
								jobj.put("tjrq", tds.get(j + 1).text());
							}
						}
					}
					if (StringUtil.isNotEmpty(start) && StringUtil.isNotEmpty(end)) {
						jobj.put("validity", "从" + start + "至" + end);
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
