package com.mapbar.traffic.score.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.DriverParser;

public class AnhuiParser implements DriverParser {

	@Override
	public String parse(String msg,DriverProfile driverProfile) {
		String ret = "";
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(msg, "UTF-8");
			Element div = doc.getElementById("con1");
			if (div == null) {
				return "";
			}
			Elements tabs = div.getElementsByTag("table");
			Elements trs = tabs.get(0).getElementsByTag("tr");
			DriverCase vc = new DriverCase();
			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;
			if (trs != null && trs.size() > 0) {
				int size = trs.size();
				int m = size / 3;
				if (m >= 1) {
					for (int i = 0; i < m; i++) {
						jcase = new JSONObject();
						jcase.put("序号", String.valueOf(i + 1));

						jcase.put("处理情况", "未处理");

						Element tr1 = trs.get(i * 3 + 0);
						Element tr2 = trs.get(i * 3 + 1);

						Elements tr1tds = tr1.getElementsByTag("td");
						String time = tr1tds.get(3).text();
						time = time.substring(0, time.length() - 2);
						String dizhi = tr1tds.get(5).text();

						String con = tr2.getElementsByTag("td").get(1).text();
						jcase.put("违法时间", time);
						jcase.put("违法地点", dizhi);
						jcase.put("违法内容", con);
						jcase.put("记分", "未知");
						jcase.put("罚款", "未知");

						if (jcase != null) {
							jcases.add(jcase);
						}
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

}
