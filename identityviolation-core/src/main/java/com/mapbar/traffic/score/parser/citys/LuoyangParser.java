package com.mapbar.traffic.score.parser.citys;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.DateUtils;

public class LuoyangParser implements DriverParser {

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {
		String strResult = "";
		try {
			DriverCase vc = new DriverCase();
			JSONObject jobj = null;

			Document doc = Jsoup.parse(strMsg);
			Element div = doc.getElementById("DirTable");
			Elements tables = div.getElementsByTag("table");
			if (tables.size() > 0) {
				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				if (trs.size() > 2) {
					trs.remove(0);
					trs.remove(0);
					for (int i = 0; i < trs.size(); i++) {
						Element tr = trs.get(i);
						jobj = new JSONObject();
						// Elements tds = tr.getElementsByTag("td");
						String carType = tr.getElementById("Txt_DirJZCX").text();
						String driverstate = tr.getElementById("Txt_DirZT").text();
						String score = tr.getElementById("Txt_DirJF").text();
						String lissuedate = tr.getElementById("Txt_DirCLRZ").text();
						String tjrq = tr.getElementById("Txt_DirSYRQ").text();
						jobj.put("carType", carType);
						jobj.put("driverstate", driverstate);
						jobj.put("score", score);
						jobj.put("lissuedate", lissuedate);
						jobj.put("tjrq", tjrq);
						jobj.put("driverlicense", driverProfile.getDriverLicense());
						jobj.put("lissuearchive", driverProfile.getLssueArchive());
						jobj.put("qinfensj", DateUtils.qingFensj(jobj));
					}

					if (!jobj.isEmpty()) {
						vc.json.put("data", jobj);
						vc.json.put("status", "ok");
						strResult = vc.toString();
					} else {
						strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}
}
