package com.mapbar.traffic.score.parser;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.parser.base.DriverParser;
import com.mapbar.traffic.score.utils.DateUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class WxBeijingParser implements DriverParser {
	@Override
	public String parse(String strMsg, DriverProfile driverProfile) {
		String strResult = "";
		// 微信接口参数错误：{"success":false,"msg":"查询驾驶员违法数据失败","flag":"-1"}
		// {"flag":"-1","msg":"输入信息不匹配","success":false}
		// {"success":false,"msg":"验证码输入有误","flag":"-2"}
		// 微信接口参数成功：
		// {"flag":"0","data":{"carType":"C1","clsj":"2007-08-02","driverIllegalRecords":[],
		// "driverid":"110102198403******","driverstate":"正常","integral":"0分","m12fsj":"",
		// "name":"赵峰","returnStatus":"SUCCESS","returnStatusMsg":"",
		// "scqfsj":"2015-08-01","tjrq":"2023-08-02","validity":"从2013-08-02至2023-08-02"},"msg":"暂无违法记录","success":true}
		// 支付宝接口参数错误： {"head":{"rtnMsg":"验签失败","rtnCode":"900901"},"body":{}}
		// 支付宝接口请求成功：
		// {"head":{"rtnMsg":"本次请求成功!","rtnCode":"000000"},"body":
		// [{"name":"赵峰","driverid":"110102198403201553","driverstate":"正常",
		// "validity":"从2013-08-02至2023-08-02","clsj":"2007-08-02","scqfsj":"2015-08-01",
		// "integral":"0","m12fsj":"","carType":"C1","tjrq":"2023-08-02","error_message":1}]}

		// /[{"id":4148452,"car_id":3268117,"status":"N","fen":6,"officer":"","occur_date":"2014-11-29 08:05:00",
		// "occur_area":"北京市海淀区北京京城皮肤病医院 北向南"
		// ,"city_id":189,"province_id":14,"code":"16250","info":"驾驶机动车违反道路交通信号灯通行的","money":200,"city_name":"北京"}]
		try {
			if (StringUtil.isNotEmpty(strMsg)) {
				JSONObject json = JSONObject.parseObject(strMsg);
				if (json.containsKey("data")) {
					JSONObject data = json.getJSONObject("data");
					// JSONArray driverIllegalRecords = data.getJSONArray("driverIllegalRecords");
					DriverCase vc = new DriverCase();

					JSONObject jobj = new JSONObject();
					jobj.put("carType", data.get("carType"));// 准驾车型
					jobj.put("lissuedate", data.get("clsj"));// 初次领证日期
					jobj.put("driverlicense", driverProfile.getDriverLicense());// 驾驶证号
					jobj.put("drivername", data.get("name"));// 姓名
					jobj.put("driverstate", data.get("driverstate"));// 驾驶证状态
					jobj.put("score", data.getString("integral").substring(0, data.getString("integral").length() - 1));// 扣分
					jobj.put("m12fsj", data.get("m12fsj"));// 满12分日期
					jobj.put("scqfsj", data.get("scqfsj"));// 上次清分日期
					jobj.put("tjrq", data.get("tjrq"));// 审验日期
					jobj.put("validity", data.get("validity")); // 有限期限
					// jobj.put("lissuearchive", data.get(""));// 驾驶证档案编号
					// jobj.put("effective", data.get(""));// 驾驶证是否长期有效
					jobj.put("qinfensj", DateUtils.qingFensj(jobj));
					if (!jobj.isEmpty()) {
						vc.json.put("data", jobj);
						vc.json.put("status", "ok");
						strResult = vc.toString();
					} else {
						strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
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
