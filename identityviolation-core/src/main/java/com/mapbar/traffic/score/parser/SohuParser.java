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

public class SohuParser implements DriverParser {
	private static Hashtable<String, String> htKeys = new Hashtable<String, String>();

	static {
		htKeys.put("datestr", "违法时间");
		htKeys.put("deductScore", "记分");
		htKeys.put("area", "违法地点");
		htKeys.put("penalty", "罚款");
		htKeys.put("content", "违法内容");
		htKeys.put("status", "处理情况");
	}

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String strResult = null;

		try {
			if (StringUtil.isNotEmpty(strMsg)) {
				JSONObject json = JSONObject.parseObject(strResult);

				if (json.containsKey("WZCX")) {
					JSONArray jelems = json.getJSONArray("WZCX");

					DriverCase vc = new DriverCase();

					JSONArray jcases = new JSONArray();
					JSONObject jcase = null;

					for (int i = 0; i < jelems.size(); i++) {
						JSONObject elem = (JSONObject) jelems.get(i);
						Set<String> keySet = elem.keySet();
						String[] strFields = (String[]) keySet.toArray();
						if (strFields != null && strFields.length > 0) {
							jcase = new JSONObject();
							jcase.put("序号", String.valueOf(i + 1));
							for (int k = 0; k < strFields.length; k++) {

								String strKey = strFields[k];
								if (htKeys.containsKey(strKey)) {
									String value = "";
									value = (elem.getString(strKey)).trim();
									if ("datestr".equals(strKey)) {
										if (value.length() > 19) {
											value = value.substring(0, 19);
										} else if (value.length() == 16) {
											value = value + ":00";
										}

									}
									if ("content".equals(strKey)) {
										if ("".equals(value) || "-".equals(value)) {
											value = "不详";
										}
									}
									if ("deductScore".equals(strKey)) {
										if ("".equals(value) || "-".equals(value)) {
											value = "0";
										}
									}
									if ("penalty".equals(strKey)) {
										if ("".equals(value) || "-".equals(value)) {
											value = "0";
										}
									}
									jcase.put(htKeys.get(strKey), value);
								}
							}

							if (jcase != null) {
								jcases.add(jcase);
							}
						}
					}

					if (jcases.size() > 0) {
						vc.json.put("data", jcases);

						vc.json.put("count", jcases.size());

						vc.json.put("status", "ok");

						strResult = vc.toString();
					}
				} else if (json.containsKey("ERRMSG") && !json.containsKey("ERRCODE")) {
					String strErrMsg = json.getString("ERRMSG");
					if (StringUtil.isNotEmpty(strErrMsg)) {
						strResult = ResultCache.toOkJsonResult(strErrMsg.trim());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;

	}

}
