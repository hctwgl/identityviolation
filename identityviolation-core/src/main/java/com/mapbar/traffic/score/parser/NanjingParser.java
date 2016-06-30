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
import com.mapbar.traffic.score.parser.base.BaseParser;
import com.mapbar.traffic.score.parser.base.DriverParser;

public class NanjingParser extends BaseParser implements DriverParser {


	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String result = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(strMsg, "UTF-8");

			Element elem = doc.getElementById("dtgCar");
			if (elem != null) {
				DriverCase vc = new DriverCase();
				JSONArray jcases = new JSONArray();
				JSONObject jcase = null;
				Elements trs = elem.getElementsByTag("tr");
				trs.remove(0);
				if (trs != null && trs.size() > 0) {
					for (int j = 0; j < trs.size(); j++) {
						jcase = new JSONObject();
						jcase.put("序号", String.valueOf(j + 1));
						jcase.put("处理情况", "未处理");
						Element ele = trs.get(j);
						Elements tds = ele.getElementsByTag("td");
						String time = tds.get(2).text();
						String dizhi = tds.get(3).text();
						String con = tds.get(1).text();
						// String fen="0";
						String money = tds.get(5).text();
						jcase.put("记分", "未知");
						jcase.put("罚款", money);
						jcase.put("违法时间", time);
						jcase.put("违法地点", dizhi);
						jcase.put("违法内容", con);

						if (jcase != null) {
							jcases.add(jcase);
						}
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
