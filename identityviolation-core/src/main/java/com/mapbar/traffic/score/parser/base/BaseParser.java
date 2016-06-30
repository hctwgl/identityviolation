package com.mapbar.traffic.score.parser.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BaseParser {

	public Map<String, String> parseHidden(String html) {
		Map<String, String> hiddenMap = new HashMap<String, String>();
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(html);
			Elements hiddens = doc.getElementsByAttributeValue("type", "hidden");
			for (int i = 0; i < hiddens.size(); i++) {
				Element ele = hiddens.get(i);
				String key = ele.attr("name");
				String value = ele.attr("value");
				hiddenMap.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hiddenMap;
	}

	public String getParamData(Map<String, String> params) {
		return getParamData(params, "UTF-8");
	}

	public String getParamData(Map<String, String> params, String charSet) {
		String param = "";
		try {
			for (Map.Entry<String, String> ent : params.entrySet()) {
				if ("".equals(param)) {
					param += ent.getKey() + "=" + URLEncoder.encode(ent.getValue(), charSet);
				} else {
					param += "&" + ent.getKey() + "=" + URLEncoder.encode(ent.getValue(), charSet);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return param;
	}
}
