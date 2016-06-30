package com.mapbar.traffic.score.parser.citys;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.DateUtils;

public class SuzhouParser implements DriverParser {

	@Override
	public String parse(String msg, DriverProfile driverProfile) {
		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

		try {
			DriverCase vc = new DriverCase();

			JSONArray jcases = new JSONArray();
			JSONObject jcase = null;

			JSONArray datas = JSONArray.parseArray(msg);
			for (int i = 0; i < datas.size(); i++) {
				JSONObject ob1 = datas.getJSONObject(i);

				jcase = new JSONObject();
				jcase.put("序号", String.valueOf(i));

				String status = ob1.getString("ZT");
				if (status.contains("未处理")) {
					status = "未处理";
					String time = ob1.getString("WFSJ");

					String time1 = time.substring(6, time.indexOf("+"));
					Date date = new Date(Long.parseLong(time1));

					String dizhi = ob1.getString("WFDZ");
					String con = ob1.getString("WFXW");
					String money = ob1.getString("FKJE");
					String jifen = ob1.getString("WFJFS");

					jcase.put("违法时间", DateUtils.getDateByFormat("yyyy-MM-dd HH:mm:ss", date));
					jcase.put("违法地点", dizhi);
					jcase.put("违法内容", con);
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

				strResult = vc.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

}
