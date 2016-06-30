package com.mapbar.traffic.score.parser.citys;

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

public class ChangzhouParser implements DriverParser {

	public String parse(String msg) {
		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

		try {
			// bgcolor="#999999"
			DriverCase vc = new DriverCase();

			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			Document doc = Jsoup.parse(msg, "GBK");
			Elements eles = doc.getElementsByAttributeValue("bgcolor", "#999999");

			if (eles.size() > 0) {

				Element table = eles.get(1);
				Elements trs = table.getElementsByTag("tr");
				if (trs.size() > 0) {
					trs.remove(0);
					for (int i = 0; i < trs.size(); i++) {
						Element tr = trs.get(i);

						jcase = new JSONObject();
						jcase.put("序号", String.valueOf(i + 1));
						jcase.put("处理情况", "未处理");
						Elements tds = tr.getElementsByTag("td");// 2014-09-2314:25:00
						String time = tds.get(3).getElementsByTag("span").get(0).text().replace(".", "-");
						String dizhi = tds.get(4).getElementsByTag("div").get(0).text();
						String con = tds.get(5).getElementsByTag("div").get(0).text();

						jcase.put("违法时间", time);
						jcase.put("违法地点", dizhi);
						jcase.put("违法内容", con);
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

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {
		// TODO Auto-generated method stub
		return null;
	}
}