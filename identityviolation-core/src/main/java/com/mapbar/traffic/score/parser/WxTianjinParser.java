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

public class WxTianjinParser implements DriverParser {

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String result = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(strMsg, "GBK");

			Element elem = doc.getElementById("alarmList");
			Elements tables = elem.getElementsByTag("table");
			if (tables != null && tables.size() != 0) {
				DriverCase vc = new DriverCase();
				JSONArray jcases = new JSONArray();
				JSONObject jcase = null;
				for (int i = 0; i < tables.size(); i++) {
					jcase = new JSONObject();
					jcase.put("序号", String.valueOf(i + 1));
					jcase.put("处理情况", "未处理");
					Element ele = tables.get(i);
					Elements trs = ele.getElementsByTag("tr");
					String time = trs.get(0).getElementsByTag("td").get(1).text();
					String dizhi = trs.get(1).getElementsByTag("td").get(1).text();
					String con = trs.get(2).getElementsByTag("td").get(1).text();
					String money = trs.get(3).getElementsByTag("td").get(1).text();
					String jifen = trs.get(4).getElementsByTag("td").get(1).text();
					jcase.put("记分", jifen);
					jcase.put("罚款", money);
					jcase.put("违法时间", time.substring(0, time.length() - 2));
					jcase.put("违法地点", dizhi);
					jcase.put("违法内容", con);

					if (jcase != null) {
						jcases.add(jcase);
					}
				}
				if (jcases.size() > 0) {
					vc.json.put("data", jcases);

					vc.json.put("count", jcases.size());

					vc.json.put("status", "ok");

					result = vc.toString();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	
	}
}
