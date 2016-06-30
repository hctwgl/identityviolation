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

public class HuBeiParser implements DriverParser {


	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String ret = "";
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(strMsg, "UTF-8");
			Elements eles = doc.getElementsByClass("9p");
			int num = 1;

			if (eles.size() > 1) {
				DriverCase vc = new DriverCase();
				JSONArray jcases = new JSONArray();
				JSONObject jcase = null;
				for (int i = 0; i < (eles.size() - 1); i++) {
					Element table = eles.get(i);
					Elements trs = table.getElementsByTag("tr");
					if (trs != null && trs.size() > 0) {
						trs.remove(0);
						int n = trs.size() / 8;
						for (int j = 0; j < n; j++) {

							jcase = new JSONObject();
							jcase.put("序号", String.valueOf(num));

							Element tr7 = trs.get(j * 8 + 7);
							String status = tr7.getElementsByTag("td").get(1).text();
							if (status.contains("未缴费")) {
								jcase.put("处理情况", "未处理");
							} else {
								continue;
							}
							Element tr1 = trs.get(j * 8 + 1);

							String time = tr1.getElementsByTag("td").get(1).text();
							time = time.replace("&nbsp;", "").replaceAll("  ", "").replaceAll("  ", "");

							Element tr2b = trs.get(j * 8 + 2);
							Element dztd = tr2b.getElementsByTag("td").get(1);
							String dizhi = dztd.text().replace("&nbsp;", "").replaceAll("  ", "").replaceAll("  ", "");

							Element tr3 = trs.get(j * 8 + 3);
							Element conTd = tr3.getElementsByTag("td").get(1);
							String content = conTd.text();
							content = content.replace("&nbsp;", "").replaceAll("  ", "").replaceAll("  ", "");

							Element tr5 = trs.get(j * 8 + 5);
							String money = tr5.getElementsByTag("td").get(1).text();
							money = money.replace("&nbsp;", "").replaceAll("  ", "").replaceAll("  ", "");

							Element tr6 = trs.get(j * 8 + 6);
							String fen = tr6.getElementsByTag("td").get(1).text();
							fen = fen.replace("&nbsp;", "").replaceAll("  ", "").replaceAll("  ", "");

							jcase.put("违法时间", time);
							jcase.put("违法地点", dizhi);
							jcase.put("违法内容", content);
							jcase.put("记分", fen);
							jcase.put("罚款", money);
							if (jcase != null) {
								jcases.add(jcase);
							}
							num++;
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

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	
	}

}
