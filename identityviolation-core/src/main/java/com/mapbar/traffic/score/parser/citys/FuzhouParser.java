package com.mapbar.traffic.score.parser.citys;

import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.parser.base.BaseParser;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class FuzhouParser extends BaseParser {

	public void parse(String msg, JSONArray jcases, int start, Vector<String> vCookies, String rootUrl) {

		try {
			Document doc = Jsoup.parse(msg, "gbk");
			Elements divs = doc.getElementsByClass("table-sr");
			if (divs.size() > 0) {

				Element div = divs.get(0);
				Elements datas = div.getElementsByClass("content");
				if (datas.size() > 0) {

					JSONObject jcase = null;

					for (int i = 0; i < datas.size(); i++) {
						Element spans = datas.get(i);
						jcase = new JSONObject();
						String time = spans.getElementsByClass("co_03").get(0).text();
						String dizhi = spans.getElementsByClass("co_04").get(0).text();
						jcase.put("处理情况", "未处理");
						jcase.put("违法时间", time);
						jcase.put("违法地点", dizhi);
						Elements ahref = spans.getElementsByClass("co_05").get(0).getElementsByTag("a");
						String url = ahref.get(0).attr("href");
						String detailUrl = rootUrl + url;
						String code = ahref.get(0).text();
						code = code.substring(0, code.length() - 4);
						String html = HttpsUtils.getURLContentsWithCookies(detailUrl, vCookies, rootUrl, null, "gbk");
						if (StringUtil.isNotEmpty(html)) {
							Document docson = Jsoup.parse(html, "gbk");
							Elements tables = docson.getElementsByClass("Normal_Table");
							Elements trs = tables.get(0).getElementsByTag("tr");
							String con = trs.get(6).getElementsByTag("td").get(0).text();
							String money = trs.get(2).getElementsByTag("td").get(0).text();
							String jifen = trs.get(4).getElementsByTag("td").get(1).text();

							jcase.put("违法内容", StringUtil.isNotEmpty(con) ? con : "未知");
							jcase.put("罚款", StringUtil.isNotEmpty(money) ? money : "未知");
							jcase.put("记分", StringUtil.isNotEmpty(jifen) ? jifen : "未知");
						} else {
							jcase.put("违法内容", "违法代码：" + code);
							jcase.put("罚款", "未知");
							jcase.put("记分", "未知");
						}
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
