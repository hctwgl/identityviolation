package com.mapbar.traffic.score.parser.citys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.StringUtil;

public class YanbianParser implements DriverParser {
	@Override
	public String parse(String strMsg,DriverProfile driverProfile) {
		String strResult = "";

		try {
			if (StringUtil.isNotEmpty(strMsg)) {
				JSONObject json = JSONObject.parseObject(strMsg);

				if (strMsg.contains("success\":true")) {
					if (json.containsKey("Infos")) {
						JSONArray jelems = json.getJSONArray("Infos");
						DriverCase vc = new DriverCase();

						JSONArray jcases = new JSONArray();
						JSONObject jcase = null;

						for (int i = 0; i < jelems.size(); i++) {
							JSONObject elem = (JSONObject) jelems.get(i);

							jcase = new JSONObject();
							jcase.put("序号", String.valueOf(i + 1));

							jcase.put("处理情况", "未处理");

							jcase.put("记分", elem.getString("wfjfs"));
							jcase.put("罚款", elem.getString("fkje"));
							// jcase.put("处理情况", "未处理");

							jcase.put("违法时间", elem.getString("wfsj"));
							jcase.put("违法地点", elem.getString("wfdz"));
							jcase.put("违法内容", elem.getString("wfnr"));

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
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

}
