package com.mapbar.traffic.score.parser.citys;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.parser.base.DriverParser;

public class WuxiParser implements DriverParser {
	@Override
	public String parse(String msg,DriverProfile driverProfile) {
		String strResult = "";
		try {
			// bgcolor="#999999"
			DriverCase vc = new DriverCase();

			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			Document doc = Jsoup.parse(msg, "UTF-8");
			Elements div = doc.getElementsByClass("cls-qa-table");
			Elements tables = div.get(0).getElementsByTag("table");
			if (tables.size() > 0) {

				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				if (trs.size() > 1) {
					trs.remove(0);
					for (int i = 0; i < trs.size(); i++) {
						Element tr = trs.get(i);

						jcase = new JSONObject();
						Elements tds = tr.getElementsByTag("td");// 2014-09-2314:25:00

						jcase.put("序号", String.valueOf(i));
						jcase.put("处理情况", "未处理");
						String time = tds.get(0).text();
						String dizhi = tds.get(1).text();
						String con = tds.get(2).text();
						String money = tds.get(3).text();
						jcase.put("违法时间", time);
						jcase.put("违法地点", dizhi);
						jcase.put("违法内容", con);
						jcase.put("记分", "未知");
						jcase.put("罚款", money);
						if (jcase != null) {
							jcases.add(jcase);
						}

					}
					if (jcases.size() > 0) {
						vc.json.put("data", jcases);

						vc.json.put("count", jcases.size());

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