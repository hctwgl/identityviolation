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

public class XuchangParser extends BaseParser {
	public String parse(String msg) {
		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

		try {
			DriverCase vc = new DriverCase();

			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			Document doc = Jsoup.parse(msg, "gbk");
			// bgcolor="#000000"
			Elements ele = doc.getElementsByAttributeValue("bgcolor", "#000000");

			if (ele.size() > 0) {

				Element table = ele.get(0);
				Elements trs = table.getElementsByTag("tr");
				if (trs.size() > 0) {
					trs.remove(0);
					for (int i = 0; i < trs.size(); i++) {
						Element tr = trs.get(i);

						jcase = new JSONObject();
						jcase.put("序号", String.valueOf(i + 1));
						jcase.put("处理情况", "未处理");
						Elements tds = tr.getElementsByTag("td");// 2014-09-2314:25:00
						String time = tds.get(2).text();
						time = time.substring(1);
						String dizhi = tds.get(3).text();
						String con = tds.get(4).text();

						jcase.put("违法时间", time.replace("/", "-"));
						jcase.put("违法地点", dizhi.substring(1));
						jcase.put("违法内容", con.substring(1));
						jcase.put("记分", "未知");
						jcase.put("罚款", "未知");
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
