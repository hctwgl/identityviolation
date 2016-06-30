package com.mapbar.traffic.score.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.StringUtil;

public class OpenCarHomeParser implements DriverParser {

	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {

		String strResult = null;

		try {
			if (StringUtil.isNotEmpty(strMsg)) {
				JSONObject jresp = JSONObject.parseObject(strResult);
				if (strMsg.contains("\"returncode\":0")) {
					DriverCase vc = new DriverCase();
					JSONArray jcases = new JSONArray();
					JSONObject jcase = null;

					JSONObject job = jresp.getJSONObject("result");
					JSONArray citys = job.getJSONArray("citys");
					for (int i = 0; i < citys.size(); i++) {
						JSONObject object = (JSONObject) citys.get(i);
						JSONArray violationdata = object.getJSONArray("violationdata");
						if (violationdata == null || violationdata.size() == 0) {
							continue;
						}
						for (int j = 0; j < violationdata.size(); j++) {
							JSONObject vio = (JSONObject) violationdata.get(j);
							jcase = new JSONObject();
							jcase.put("序号", String.valueOf(j + 1));
							jcase.put("处理情况", vio.getString("processstatustext").split(",")[0]);
							jcase.put("记分", String.valueOf(vio.get("score")));
							jcase.put("罚款", String.valueOf(vio.get("pay")));
							jcase.put("违法时间", vio.getString("time") + ":00");
							jcase.put("违法地点", vio.getString("location"));
							jcase.put("违法内容", vio.getString("content"));

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
					} else {
						strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					}
				}
				// else if(strMsg.contains("\"returncode\":-81")){
				// return ResultCache.toErrJsonResult("发动机号错误！");
				// }else if(strMsg.contains("\"returncode\":-82")){
				// return ResultCache.toErrJsonResult("发动机号错误！");
				// }else if(strMsg.contains("\"returncode\":-83")){
				// return ResultCache.toErrJsonResult("车架号错误！");
				// }
				// else if(strMsg.contains("\"returncode\":-84")){
				// return ResultCache.toErrJsonResult("车辆信息错误！");
				// }else if(strMsg.contains("\"returncode\":-85")){
				// return ResultCache.toErrJsonResult("车辆不存在！");
				// }
				else {
					return ResultCache.toErrJsonResult("服务器繁忙，请稍后再试！");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;

	}

}
