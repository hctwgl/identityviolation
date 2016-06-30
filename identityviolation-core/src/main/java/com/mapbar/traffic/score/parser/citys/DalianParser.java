package com.mapbar.traffic.score.parser.citys;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.parser.base.BaseParser;
import com.mapbar.traffic.score.utils.DateUtils;

public class DalianParser extends BaseParser {

	public void parse(String msg, JSONObject jobj, DriverProfile driverProfile) {

		try {
			Document doc = Jsoup.parse(msg);
			Elements tables = doc.getElementsByTag("table");
			if (tables.size() > 0) {
				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				if (trs.size() > 0) {
					Element tr = trs.get(1);
					Elements tds = tr.getElementsByTag("td");
					String drivername = tds.get(0).text();
					String driverlicense = tds.get(1).text();
					String score = tds.get(2).text();

					jobj.put("driverlicense", driverlicense);// 驾驶证号
					jobj.put("drivername", drivername);// 姓名
					jobj.put("score", score);// 扣分
					jobj.put("lissuearchive", driverProfile.getLssueArchive());// 驾驶证档案编号
					jobj.put("qinfensj", DateUtils.qingFensj(jobj));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
