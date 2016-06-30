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

public class WxSuzhouParser extends BaseParser implements DriverParser {

	@Override
	public String parse(String msg,DriverProfile driverProfile) {
		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

		try {
			DriverCase vc = new DriverCase();

			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			Document doc = Jsoup.parse(msg, "UTF-8");
			Element ele = doc.getElementById("resultdata");
			Elements tables = ele.getElementsByClass("table1");
			if (tables.size() > 1) {
				// ables.remove(0);
				for (int i = 1; i < tables.size(); i++) {
					Element table = tables.get(i);
					jcase = new JSONObject();
					jcase.put("序号", String.valueOf(i));

					Elements trs = table.getElementsByTag("tr");
					String status = trs.get(2).getElementsByTag("td").get(0).text();
					if (status.contains("未处理")) {
						jcase.put("处理情况", "未处理");
					} else {
						jcase.put("处理情况", "已处理");
					}
					String time = trs.get(3).getElementsByTag("td").get(0).text();
					String dizhi = trs.get(5).getElementsByTag("td").get(0).text();
					String con = trs.get(4).getElementsByTag("td").get(0).text();
					String money = trs.get(6).getElementsByTag("td").get(0).text();
					jcase.put("违法时间", time);
					jcase.put("违法地点", dizhi);
					jcase.put("违法内容", con);
					jcase.put("记分", "未知");
					jcase.put("罚款", money);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

}
