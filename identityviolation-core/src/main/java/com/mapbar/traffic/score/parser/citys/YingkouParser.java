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
import com.mapbar.traffic.score.utils.StringUtil;

public class YingkouParser implements DriverParser {
	@Override
	public String parse(String msg,DriverProfile driverProfile) {
		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

		try {
			// bgcolor="#999999"
			DriverCase vc = new DriverCase();

			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			Document doc = Jsoup.parse(msg, "GBK");

			Elements tables = doc.getElementsByAttributeValue("height", "26");
			if (tables.size() > 0) {

				for (int i = 0; i < tables.size(); i++) {
					Element table = tables.get(i);
					Elements tr = table.getElementsByTag("tr");

					Elements tds = tr.get(0).getElementsByTag("td");// 2014-09-2314:25:00
					jcase = new JSONObject();
					jcase.put("序号", String.valueOf(i + 1));
					String time = tds.get(3).text();
					String dizhi = tds.get(2).text();
					String money = tds.get(4).text();
					String con = tr.get(1).getElementsByTag("font").get(0).text();
					jcase.put("违法时间", time.replace(" ", ""));
					jcase.put("处理情况", "未处理");
					jcase.put("违法地点", dizhi);
					jcase.put("违法内容", con.replace("违法行为：", ""));
					jcase.put("记分", "未知");
					jcase.put("罚款", StringUtil.isNotEmpty(money) ? money : "未知");
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
