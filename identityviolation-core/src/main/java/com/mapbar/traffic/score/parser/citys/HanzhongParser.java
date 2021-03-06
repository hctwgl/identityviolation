package com.mapbar.traffic.score.parser.citys;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.BaseParser;

public class HanzhongParser extends BaseParser {
	public String parse(String msg) {
		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
		// bordercolor="#B1D8F8"
		try {
			// bgcolor="#999999"
			DriverCase vc = new DriverCase();

			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			Document doc = Jsoup.parse(msg, "UTF-8");

			Elements tables = doc.getElementsByAttributeValue("bordercolor", "#B1D8F8");
			if (tables.size() > 0) {

				Element table = tables.get(1);
				Elements trs = table.getElementsByTag("tr");
				if (trs.size() > 1) {
					trs.remove(0);
					int n = 0;
					for (int i = 0; i < trs.size(); i++) {
						Element tr = trs.get(i);

						Elements tds = tr.getElementsByTag("td");// 2014-09-2314:25:00
						String status = tds.get(6).text();
						if ("未交款".equals(status)) {
							n++;
							jcase = new JSONObject();
							jcase.put("序号", String.valueOf(n));
							String time = tds.get(2).text();

							String dizhi = tds.get(3).text();
							String con = tds.get(4).text();

							jcase.put("处理情况", "未处理");
							jcase.put("违法时间", time.subSequence(0, time.length() - 2));
							jcase.put("违法地点", dizhi);
							jcase.put("违法内容", con);
							jcase.put("记分", "未知");
							jcase.put("罚款", "未知");
							if (jcase != null) {
								jcases.add(jcase);
							}

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
