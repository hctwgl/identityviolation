package com.mapbar.traffic.score.parser;

import java.util.Hashtable;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.StringUtil;

public class GuangzhouParser implements DriverParser {
	private static Hashtable<String, String> htKeys = new Hashtable<String, String>();

	static {
		htKeys.put("WFSJ", "违法时间");
		htKeys.put("WFJFS", "记分");
		htKeys.put("WFDZ", "违法地点");
		htKeys.put("FKJE", "罚款");
		htKeys.put("WFXWMC", "违法内容");
		// htKeys.put("status", "处理情况");
	}

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {
		String strResult = "";
		try {
			if (StringUtil.isNotEmpty(strMsg)) {
				JSONObject json = JSONObject.parseObject(strResult);

				if (json.containsKey("data")) {
					JSONArray jelems = json.getJSONArray("data");

					DriverCase vc = new DriverCase();

					JSONArray jcases = new JSONArray();
					JSONObject jcase = null;

					for (int i = 0; i < jelems.size(); i++) {
						JSONObject elem = (JSONObject) jelems.get(i);

						Set<String> key = htKeys.keySet();
						jcase = new JSONObject();
						jcase.put("序号", String.valueOf(i + 1));
						jcase.put("处理情况", "未处理");
						for (String k : key) {
							String value = elem.getString(k) == null ? "" : elem.getString(k);
							value = value.trim();
							if ("WFSJ".equals(k)) {
								if (value.length() > 19) {
									value = value.substring(0, 19);
								}
							}
							if ("WFXWMC".equals(k)) {
								if ("".equals(value) || "-".equals(value)) {
									value = "不详";
								}
							}
							if ("WFJFS".equals(k)) {
								if ("".equals(value) || "-".equals(value)) {
									value = "未知";
								}
							}
							if ("FKJE".equals(k)) {
								if ("".equals(value) || "-".equals(value)) {
									value = "未知";
								}
							}
							jcase.put(htKeys.get(k), value);
						}

						if (jcase != null) {
							jcases.add(jcase);
						}

					}

					if (jcases.size() > 0) {
						vc.json.put("data", jcases);

						vc.json.put("count", jcases.size());

						vc.json.put("status", "ok");

						strResult = vc.toString();
					}
				} else {
					strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

}
