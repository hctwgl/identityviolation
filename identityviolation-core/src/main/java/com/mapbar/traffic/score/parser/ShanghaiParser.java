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

public class ShanghaiParser implements DriverParser {

	@Override
	public String parse(String msg,DriverProfile driverProfile) {
		String ret = "";
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK2312");
			Document doc = Jsoup.parse(msg);
			Elements eles = doc.getElementsByClass("chinses1");
			int num = 1;
			DriverCase vc = new DriverCase();
			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;
			for (int i = 0; i < eles.size(); i++) {
				Element ele = eles.get(i);
				if (ele.tagName().equals("table") && !ele.toString().contains("验证码")) {

					Elements trs = ele.getElementsByTag("tr");
					if (trs.size() > 3) {
						trs.remove(0);
						trs.remove(0);
						trs.remove(0);
						trs.remove(trs.size() - 1);
						if (trs != null && trs.size() > 0) {
							if (ele.toString().contains("本市电子监控查询结果") || ele.toString().contains("本市违法停车查询结果")) {

								int n = trs.size() / 5;
								for (int j = 0; j < n; j++) {

									jcase = new JSONObject();
									jcase.put("序号", String.valueOf(num));
									jcase.put("处理情况", "未处理");
									Element tr1 = trs.get(j * 5);

									Element timeTd = tr1.getElementsByTag("td").get(2);
									String tm = timeTd.getElementsByTag("div").text() + ":00";

									Element tr2 = trs.get(j * 5 + 1);
									Element dztd = tr2.getElementsByTag("td").get(1);
									String dizhi = dztd.getElementsByTag("div").text();

									Element tr3 = trs.get(j * 5 + 2);
									Element conTd = tr3.getElementsByTag("td").get(1);
									String content = conTd.text();
									String codeDiv = conTd.getElementsByTag("div").text();
									content = content.replaceAll(conTd.getElementsByTag("div").toString(), codeDiv);
									jcase.put("违法时间", tm);
									jcase.put("违法地点", dizhi);
									jcase.put("违法内容", content);
									jcase.put("记分", "未知");
									jcase.put("罚款", "未知");
									if (jcase != null) {
										jcases.add(jcase);
									}
									num++;
								}
							}
							if (ele.toString().contains("外地电子监控查询结果")) {
								int n = trs.size() / 6;
								for (int j = 0; j < n; j++) {

									jcase = new JSONObject();
									jcase.put("序号", String.valueOf(num));
									jcase.put("处理情况", "未处理");
									Element tr1 = trs.get(j * 6);

									Element timeTd = tr1.getElementsByTag("td").get(2);
									String tm = timeTd.getElementsByTag("div").text() + ":00";

									Element tr2a = trs.get(j * 6 + 1);
									Element ct = tr2a.getElementsByTag("td").get(3);
									String ctname = ct.getElementsByTag("div").text();

									Element tr2b = trs.get(j * 6 + 2);
									Element dztd = tr2b.getElementsByTag("td").get(1);
									String dizhi = dztd.getElementsByTag("div").text();

									Element tr3 = trs.get(j * 6 + 3);
									Element conTd = tr3.getElementsByTag("td").get(1);
									String content = conTd.text();
									String codeDiv = conTd.getElementsByTag("div").text();
									content = content.replaceAll(conTd.getElementsByTag("div").toString(), codeDiv);
									jcase.put("违法时间", tm);
									jcase.put("违法地点", ctname + "市 " + dizhi);
									jcase.put("违法内容", content);
									jcase.put("记分", "未知");
									jcase.put("罚款", "未知");
									if (jcase != null) {
										jcases.add(jcase);
									}
									num++;
								}
							}
						}

					} else {
						continue;
					}
				}
			}
			if (jcases.size() > 0) {
				
				vc.json.put("data", jcases);
				vc.json.put("count", jcases.size());
				vc.json.put("lissuearchive", driverProfile.getLssueArchive());
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
