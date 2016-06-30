package com.mapbar.traffic.score.parser;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.BaseParser;
import com.mapbar.traffic.score.parser.base.DriverParser;

public class ChengduParser extends BaseParser implements DriverParser {

	@Override
	public String parse(String strResult,DriverProfile driverProfile) {
		String ret = "";
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(strResult, "UTF-8");
			Element div = doc.getElementById("ContentPlaceHolder1_Panel1");
			if (div == null) {
				return "";
			}
			Elements trs = div.getElementsByTag("tr");
			DriverCase vc = new DriverCase();
			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;
			if (trs != null && trs.size() > 2) {
				trs.remove(0);
				trs.remove(0);
				for (int i = 0; i < trs.size(); i++) {
					jcase = new JSONObject();
					jcase.put("序号", String.valueOf(i + 1));
					jcase.put("记分", "未知");
					jcase.put("罚款", "未知");
					// jcase.put("处理情况", "未处理");
					Element ele = trs.get(i);
					Elements tds = ele.getElementsByTag("td");
					String time = tds.get(0).text() + ":00";
					String dizhi = tds.get(1).text();
					String con = tds.get(2).text();
					String des = tds.get(3).text();
					jcase.put("违法时间", time);
					jcase.put("违法地点", dizhi);
					jcase.put("违法内容", con);
					jcase.put("处理情况", des);

					if (jcase != null) {
						jcases.add(jcase);
					}
				}
			}
			if (jcases.size() > 0) {
				vc.json.put("data", jcases);

				vc.json.put("count", jcases.size());

				vc.json.put("status", "ok");

				ret = vc.toString();
			} else {
				ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public Map<String, String> parseHidden(String html) {
		Map<String, String> hiddenMap = new HashMap<String, String>();

		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(html, "UTF-8");
			Elements hiddens = doc.getElementsByAttributeValue("type", "hidden");
			for (int i = 0; i < hiddens.size(); i++) {
				Element ele = hiddens.get(i);
				String key = ele.attr("name");
				String value = ele.attr("value");
				// System.out.println(key+":"+value);
				hiddenMap.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(hiddenMap);
		return hiddenMap;
	}

}
