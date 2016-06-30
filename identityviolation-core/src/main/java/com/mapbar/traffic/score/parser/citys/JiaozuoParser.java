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
import com.mapbar.traffic.score.parser.base.BaseParser;
import com.mapbar.traffic.score.parser.base.DriverParser;

public class JiaozuoParser extends BaseParser implements DriverParser {


	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

		try {
			// bgcolor="#999999"
			DriverCase vc = new DriverCase();

			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			Document doc = Jsoup.parse(strMsg, "GBK");// border=1
			Element table1 = doc.getElementById("GridView1");
			Element table2 = doc.getElementById("GridView2");

			if (table1 != null) {
				if (!table1.text().contains("恭喜您，您的车没有违章记录")) {
					Elements trs = table1.getElementsByTag("tr");
					if (trs.size() > 1) {
						trs.remove(0);
						for (int i = 0; i < trs.size(); i++) {
							Element tr = trs.get(i);

							jcase = new JSONObject();
							Elements tds = tr.getElementsByTag("td");// 2014-09-2314:25:00

							jcase.put("序号", String.valueOf(i + 1));
							jcase.put("处理情况", "未处理");
							String time = tds.get(2).text();// 2014年01月03日13时37分

							String dizhi = tds.get(3).text();
							String con = "未知";

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
			}
			if (table2 != null) {
				if (!table2.text().contains("恭喜您，您的车没有违章记录")) {
					Elements trs = table2.getElementsByTag("tr");
					if (trs.size() > 1) {
						trs.remove(0);
						for (int i = 0; i < trs.size(); i++) {
							Element tr = trs.get(i);

							jcase = new JSONObject();
							Elements tds = tr.getElementsByTag("td");// 2014-09-2314:25:00

							jcase.put("序号", String.valueOf(i + 1));
							jcase.put("处理情况", "未处理");
							String time = tds.get(2).text();// 2014年01月03日13时37分

							String dizhi = tds.get(3).text();
							String con = "未知";

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
			}
			if (jcases.size() > 0) {
				vc.json.put("data", jcases);

				vc.json.put("count", jcases.size());

				vc.json.put("status", "ok");

				strResult = vc.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	
	}
}
