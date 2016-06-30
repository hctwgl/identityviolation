package com.mapbar.traffic.score.parser;

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

public class ShenyangParser implements DriverParser {

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {
		String ret = "";
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(strMsg);
			Elements tab = doc.getElementsByClass("table");
			if (tab == null) {
				return "";
			}
			Elements trs = tab.get(0).getElementsByTag("tr");
			DriverCase vc = new DriverCase();
			JSONObject jobj = null;
			if (trs != null && trs.size() > 0) {
				trs.remove(0);
				trs.remove(0);
				for (int i = 0; i < trs.size(); i++) {
					jobj = new JSONObject();
					jobj.put("序号", String.valueOf(i + 1));

					// jcase.put("处理情况", "未处理");
					Element ele = trs.get(i);
					Elements tds = ele.getElementsByTag("td");
					String time = tds.get(1).text() + ":00";
					String dizhi = tds.get(2).text();
					String con = tds.get(3).text();
					String des = "未处理";
					dizhi = dizhi.substring(1);
					con = con.substring(1);
					time = time.substring(0, 10) + " " + time.substring(10);

					String fenMoney = con.substring(con.indexOf("(") + 1, con.indexOf(")"));

					con = con.substring(0, con.indexOf("(") - 1);
					String fen = fenMoney.split("，")[0];
					String money = fenMoney.split("，")[1];
					jobj.put("违法时间", time);
					jobj.put("违法地点", dizhi);
					jobj.put("违法内容", con);
					jobj.put("记分", fen.replace("分", ""));
					jobj.put("罚款", money.replace("元", ""));
					jobj.put("处理情况", des);
					jobj.put("qinfensj", DateUtils.qingFensj(jobj));
				}
			}
			if (!jobj.isEmpty()) {
				vc.json.put("data", jobj);
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
