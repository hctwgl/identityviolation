package com.mapbar.traffic.score.parser;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.BaseParser;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.StringUtil;

public class Car100Parser extends BaseParser implements DriverParser {

	@Override
	public String parse(String msg, DriverProfile driverProfile) {
		String strResult = ResultCache.toErrJsonResult("服务维护中, 请稍候再试.");
		;

		try {
			if (StringUtil.isNotEmpty(msg) && msg.contains("tr")) {
				Parser parser = Parser.createParser(msg, "UTF-8");
				NodeList nodes = parser.parse(null);
				DriverCase vc = new DriverCase();
				JSONArray jcases = new JSONArray();
				JSONObject jcase = null;
				for (int i = 0; i < nodes.size(); i++) {
					jcase = new JSONObject();
					Node node = nodes.elementAt(i);
					NodeList children = node.getChildren();
					Node time = children.elementAt(0);
					jcase.put("序号", String.valueOf(i + 1));
					jcase.put("处理情况", "未处理");
					String t = time.toPlainTextString();
					String dizhi = children.elementAt(1).toPlainTextString();
					String content = children.elementAt(2).toPlainTextString();

					String fen = children.elementAt(3).toPlainTextString();
					String money = children.elementAt(4).toPlainTextString();

					jcase.put("违法时间", t);
					jcase.put("违法地点", dizhi);
					jcase.put("违法内容", content);
					jcase.put("记分", fen);
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
				} else {
					strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				}
			} else {
				strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	public String getKeys(String html) {
		try {
			Document doc = Jsoup.parse(html, "UTF-8");

			Element hidden = doc.getElementById("keys");

			return hidden.attr("value");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
