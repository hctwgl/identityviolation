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
import com.mapbar.traffic.score.utils.StringUtil;

public class XinjiangParser extends BaseParser implements DriverParser {

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(strMsg, "GBK");

			Elements tables = doc.getElementsByClass("wfzx_t2");
			DriverCase vc = new DriverCase();
			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			if (tables != null && tables.size() > 0) {
				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				if (trs != null && trs.size() > 0) {
					for (int j = 0; j < trs.size(); j++) {
						jcase = new JSONObject();
						jcase.put("序号", String.valueOf(j + 1));

						Element ele = trs.get(j);
						Elements tds = ele.getElementsByTag("td");
						String time = tds.get(4).text();
						time = time + ":00";
						String dizhi = tds.get(6).getElementsByTag("a").get(0).text();
						String con = tds.get(5).getElementsByTag("a").get(0).text();
						String fen = tds.get(1).text();
						String money = tds.get(0).text();

						jcase.put("记分", StringUtil.isNotEmpty(fen) ? fen : "未知");
						jcase.put("罚款", StringUtil.isNotEmpty(money) ? money : "未知");
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

					ret = vc.toString();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	
	}
}
