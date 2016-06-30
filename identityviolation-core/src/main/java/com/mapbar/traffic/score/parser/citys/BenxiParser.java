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

public class BenxiParser extends BaseParser {

	public String parse(String msg) {
		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

		try {

			DriverCase vc = new DriverCase();

			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			Document doc = Jsoup.parse(msg, "UTF-8");

			Elements tables = doc.getElementsByClass("table");
			int n = 0;
			if (tables.size() > 0) {
				tables.remove(0);

				Element table1 = tables.get(0);
				if (!table1.text().contains("无处罚记录")) {
					Elements bgs = table1.getElementsByClass("bg");
					for (int i = 0; i < bgs.size(); i++) {
						Element bg = bgs.get(i);
						Elements lis = bg.getElementsByTag("li");

						String status = lis.get(11).text();
						if (!"已缴款".equals(status)) {
							jcase = new JSONObject();
							String time = lis.get(4).text();
							String dizhi = lis.get(5).text();
							String jifen = lis.get(8).text();
							String money = lis.get(9).text();
							String content = "违法代码：" + lis.get(6).text();
							jcase.put("序号", String.valueOf(++n));
							jcase.put("处理情况", "未处理");
							jcase.put("违法时间", time);
							jcase.put("违法地点", dizhi);
							jcase.put("违法内容", content);
							jcase.put("记分", jifen);
							jcase.put("罚款", money);
							if (jcase != null) {
								jcases.add(jcase);
							}
						}
					}
				}
				Element table2 = tables.get(1);
				if (!table2.text().contains("无处罚记录")) {
					Elements bgs = table2.getElementsByClass("bg");
					for (int i = 0; i < bgs.size(); i++) {
						//Element bg = bgs.get(i);
						//Elements lis = bg.getElementsByTag("li");

						// String status = lis.get(11).text();
						// if(!"已缴款".equals(status)){
						// jcase = new JSONObject();
						// String time = lis.get(1).text();
						// String dizhi = lis.get(2).text();
						// String jifen = "未知";
						// String money = "未知";
						// String content = "违法代码："+lis.get(2).text();
						// jcase.put("序号", String.valueOf( ++n));
						// jcase.put("处理情况", "未处理");
						// jcase.put("违法时间", time);
						// jcase.put("违法地点", dizhi);
						// jcase.put("违法内容", content);
						// jcase.put("记分", jifen);
						// jcase.put("罚款",money);
						// if (jcase != null) {
						// jcases.put(jcase);
						// }
						// }
					}
				}
				Element table3 = tables.get(2);
				if (!table3.text().contains("无处罚记录")) {
					Elements bgs = table3.getElementsByClass("bg");
					for (int i = 0; i < bgs.size(); i++) {
						Element bg = bgs.get(i);
						Elements lis = bg.getElementsByTag("li");

						String status = lis.get(11).text();
						if (!"已缴款".equals(status)) {
							jcase = new JSONObject();
							String time = lis.get(3).text();
							String dizhi = lis.get(4).text();
							String jifen = "未知";
							String money = lis.get(8).text();
							String content = "违法代码：" + lis.get(5).text();
							jcase.put("序号", String.valueOf(++n));
							jcase.put("处理情况", "未处理");
							jcase.put("违法时间", time);
							jcase.put("违法地点", dizhi);
							jcase.put("违法内容", content);
							jcase.put("记分", jifen);
							jcase.put("罚款", money);
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
