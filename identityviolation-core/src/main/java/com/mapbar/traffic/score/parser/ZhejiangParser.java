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

public class ZhejiangParser implements DriverParser {

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String ret = "";
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(strMsg, "GBK");// bgcolor=""
			Elements tabs = doc.getElementsByAttributeValue("bgcolor", "#81c3e5");
			if (tabs == null) {
				return "";
			}

			DriverCase vc = new DriverCase();
			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			if (tabs != null && tabs.size() > 0) {

				for (int i = 0; i < tabs.size(); i++) {
					jcase = new JSONObject();
					Element tab = tabs.get(i);
					Elements trs = tab.getElementsByTag("tr");

					jcase.put("序号", String.valueOf(i + 1));

					// jcase.put("处理情况", "未处理");

					Element tr1 = trs.get(1);
					Element tr2 = trs.get(2);
					Elements tr1d = tr1.getElementsByTag("td");
					Elements tr2d = tr2.getElementsByTag("td");

					String dizhi = tr1d.get(1).text();
					String con = tr1d.get(3).text();
					String des = tr2d.get(3).text();
					String time = tr2d.get(1).text();

					jcase.put("违法时间", time);
					jcase.put("违法地点", dizhi);
					jcase.put("违法内容", con);
					jcase.put("记分", "未知");
					jcase.put("罚款", "未知");
					jcase.put("处理情况", des);
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
			} else {
				ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;

	}

}
