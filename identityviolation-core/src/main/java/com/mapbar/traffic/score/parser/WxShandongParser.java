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
import com.mapbar.traffic.score.utils.StringUtil;

public class WxShandongParser implements DriverParser {

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String ret = "";
		try {
			// Document doc = Jsoup.parse(new File("G:\\workspace\\violation-core-1.1.x\\src\\shanghai.jsp"),"GBK");
			Document doc = Jsoup.parse(strMsg, "UTF-8");
			Elements divs = doc.getElementsByClass("wz_xinxi");
			if (divs == null || divs.size() == 0) {
				return "";
			}
			Element div = divs.get(0);
			Elements lis = div.getElementsByTag("li");

			DriverCase vc = new DriverCase();
			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;
			if (lis != null && lis.size() > 0) {
				int size = lis.size();

				for (int i = 0; i < (size - 1); i++) {
					jcase = new JSONObject();
					Element li = lis.get(i);

					jcase.put("序号", String.valueOf(i + 1));
					Element left = li.getElementsByClass("wz_left").get(0);
					jcase.put("处理情况", "未知");
					String time = left.getElementsByClass("wz_time").get(0).text();
					if (!StringUtil.isNotEmpty(time)) {
						continue;
					}
					String con = left.getElementsByClass("wz_lx").get(0).text();
					String dizhi = left.getElementsByClass("wz_con").get(0).text();
					jcase.put("违法时间", time);
					jcase.put("违法地点", dizhi);
					jcase.put("违法内容", con);

					Element right = li.getElementsByClass("wz_right").get(0);
					Elements as = right.getElementsByTag("a");
					String jifen = as.get(0).text().replace("<span>", "").replace("</span>", "");
					String money = as.get(1).text().replace("<span>", "").replace("</span>", "");
					jcase.put("记分", jifen);
					jcase.put("罚款", money);

					if (jcase != null) {
						jcases.add(jcase);
					}
				}

			}
			if (jcases.size() > 0) {
				vc.json.put("data", jcases);

				vc.json.put("count", jcases.size());

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
