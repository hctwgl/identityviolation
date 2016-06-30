package com.mapbar.traffic.score.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.StringUtil;

public class CheshouyeParser implements DriverParser {
	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {
		String strResult = ResultCache.toErrJsonResult("服务维护中, 请稍候再试.");
		// /[{"id":4148452,"car_id":3268117,"status":"N","fen":6,"officer":"","occur_date":"2014-11-29 08:05:00",
		// "occur_area":"北京市海淀区北京京城皮肤病医院 北向南"
		// ,"city_id":189,"province_id":14,"code":"16250","info":"驾驶机动车违反道路交通信号灯通行的","money":200,"city_name":"北京"}]
		try {
			if (StringUtil.isNotEmpty(strMsg)) {
				JSONObject json = JSONObject.parseObject(strResult);

				if (json.containsKey("historys")) {
					JSONArray jelems = json.getJSONArray("historys");

					DriverCase vc = new DriverCase();

					JSONArray jcases = new JSONArray();
					JSONObject jcase = null;

					for (int i = 0; i < jelems.size(); i++) {
						JSONObject elem = (JSONObject) jelems.get(i);

						jcase = new JSONObject();
						jcase.put("序号", String.valueOf(i + 1));

						String status = elem.getString("status");
						if ("N".equals(status)) {
							jcase.put("处理情况", "未处理");
						} else {
							jcase.put("处理情况", "已处理");
						}
						jcase.put("记分", elem.getString("fen"));
						jcase.put("罚款", elem.getString("money"));
						// jcase.put("处理情况", "未处理");

						jcase.put("违法时间", elem.getString("occur_date"));
						jcase.put("违法地点", elem.getString("occur_area"));
						jcase.put("违法内容", elem.getString("info") + "(" + elem.getString("code") + ")");

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
