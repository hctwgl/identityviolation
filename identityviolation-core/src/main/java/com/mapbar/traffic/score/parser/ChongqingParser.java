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

public class ChongqingParser implements DriverParser {

	@Override
	public String parse(String strResult, DriverProfile driverProfile) {
		String ret = "";
		try {
			Document doc = Jsoup.parse(strResult, "UTF-8");
			Elements tables = doc.getElementsByTag("table");
			if (tables.size() > 0) {

				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				if (trs.size() > 0) {
					DriverCase vc = new DriverCase();
					JSONArray jcases = new JSONArray();
					JSONObject jcase = null;

					trs.remove(0);
					for (int i = 0; i < trs.size(); i++) {
						Element tr = trs.get(i);

						if (tr.html().contains("未处理")) {
							jcase = new JSONObject();
							jcase.put("序号", String.valueOf(i + 1));

							jcase.put("处理情况", "未处理");

							Elements tds = tr.getElementsByTag("td");
							// String time = tds.get(1).text() + " 00:00:00";
							String dizhi = tds.get(2).text();
							String money = tds.get(3).text();
							String fen = tds.get(4).text();
							Element td6 = tds.get(6);
							Elements ahtml = td6.getElementsByTag("a");
							Element aele = ahtml.get(0);
							String content = aele.attr("onclick");

							String con = content.split(",")[7];
							con = con.replace("\"", "");
							// con=con.substring(1, content.split(",")[7].length()-1);
							String time = content.split(",")[2];
							time = time.replace("\"", "");
							time = time.replaceAll("\\.", "-") + ":00";
							jcase.put("违法时间", time.substring(1, time.length()));
							jcase.put("违法地点", dizhi);
							jcase.put("违法内容", con.substring(1, con.length()));
							jcase.put("记分", fen);
							jcase.put("罚款", money.replace("(元)", ""));
							if (jcase != null) {
								jcases.add(jcase);
							}
						} else {
							continue;
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}


}
