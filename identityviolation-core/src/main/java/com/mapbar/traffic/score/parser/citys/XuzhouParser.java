package com.mapbar.traffic.score.parser.citys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.parser.base.BaseParser;

public class XuzhouParser extends BaseParser {
	public void parse(String msg, JSONArray jcases) {

		try {
			int start = jcases.size();

			JSONArray array = JSONArray.parseArray(msg);
			if (array != null && array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					++start;
					JSONObject job = array.getJSONObject(i);

					JSONObject jcase = new JSONObject();
					jcase.put("序号", String.valueOf(start));
					jcase.put("处理情况", "未处理");
					String time = job.getString("wfsj");
					String dizhi = job.getString("wfdd");
					String con = job.getString("wfxw");
					String money = job.getString("wfje");
					String jifen = job.getString("wfjf");
					jcase.put("违法时间", time);
					jcase.put("违法地点", dizhi);
					jcase.put("违法内容", con);
					jcase.put("记分", jifen);
					jcase.put("罚款", money);

					jcases.add(jcase);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
