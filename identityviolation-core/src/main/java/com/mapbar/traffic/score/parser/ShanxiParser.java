package com.mapbar.traffic.score.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.parser.base.PageParser;
import com.mapbar.traffic.score.utils.DateUtils;

public class ShanxiParser implements PageParser {

	public void parse(String result, JSONObject jobj, DriverProfile driverProfile) {
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parseBodyFragment(result);// bgcolor=""
			Elements trs = doc.getElementsByClass("if_tr");
			if (trs == null) {
				return;
			}
			if (trs != null) {
				for (int i = 0; i < trs.size(); i++) {
					Element tr = trs.get(i);
					Elements tds = tr.getElementsByTag("td");
					// jcase.put("序号", String.valueOf(++start));
					String carType = tds.get(0).text();
					String yxqs = tds.get(1).text();
					String yxqz = tds.get(2).text();
					String validity = "从" + yxqs + "至" + yxqz;
					String lissuedate = tds.get(3).text();
					String tjrq = tds.get(4).text();
					String score = tds.get(5).text();
					String scqfsj = tds.get(6).text();
					String driverstate = tds.get(7).text();

					jobj.put("carType", carType);// 准驾车型
					jobj.put("lissuedate", lissuedate);// 初次领证日期
					jobj.put("driverlicense", driverProfile.getDriverLicense());// 驾驶证号
					jobj.put("drivername", driverProfile.getDriverName());// 姓名
					jobj.put("driverstate", driverstate);// 驾驶证状态
					jobj.put("score", score);// 扣分
					jobj.put("scqfsj", scqfsj);// 上次清分日期
					jobj.put("tjrq", tjrq);// 审验日期
					jobj.put("validity", validity); // 有限期限
					jobj.put("lissuearchive", driverProfile.getLssueArchive());// 驾驶证档案编号
					jobj.put("qinfensj", DateUtils.qingFensj(jobj));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
