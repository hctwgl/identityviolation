package com.mapbar.traffic.score.parser.citys;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.parser.base.BaseParser;

public class XiamenParser extends BaseParser {
	public void parse(String msg, JSONArray jcases, int start) {

		try {
			Document doc = Jsoup.parse(msg, "UTF-8");
			Element div = doc.getElementById("cx_id_1");
			Elements eles = div.getElementsByClass("fot9");
			if (eles.size() > 0) {

				Element table = eles.get(1);
				Elements body = table.getElementsByTag("TBODY");
				if (body.size() > 0) {
					Elements trs = body.get(0).getElementsByTag("TR");

					JSONObject jcase = null;

					trs.remove(0);
					for (int i = 0; i < (trs.size() - 3); i++) {
						Element tr = trs.get(i);

						jcase = new JSONObject();

						Elements tds = tr.getElementsByTag("TD");// 2014-09-2314:25:00
						String status = tds.get(0).text();
						if (!status.contains("未处理")) {
							continue;
						}
						jcase.put("处理情况", "未处理");

						jcase.put("序号", String.valueOf(++start));
						String time = tds.get(1).text();
						String dizhi = tds.get(2).text();
						String con = tds.get(3).text();
						String money = tds.get(4).text();
						String jifen = tds.get(5).text();
						jcase.put("违法时间", time);
						jcase.put("违法地点", dizhi);
						jcase.put("违法内容", con);
						jcase.put("记分", jifen.replace("扣", "").replace("分", ""));
						jcase.put("罚款", money.replace("元", ""));
						if (jcase != null) {
							jcases.add(jcase);
						}

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
