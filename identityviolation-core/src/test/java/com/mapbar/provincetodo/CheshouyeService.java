package com.mapbar.provincetodo;

import java.util.Date;
import java.util.Vector;

import org.apache.http.HttpHost;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.parser.CheshouyeParser;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.ConfigUtils;
import com.mapbar.traffic.score.utils.HttpClientUtil;
import com.mapbar.traffic.score.utils.StringUtil;

public class CheshouyeService implements Transfer {
	private String GET_PROVINCE_CONFIG = "http://www.cheshouye.com/api/weizhang/get_province_config?";
	private String GET_VIOLATION_URL_OPEN = "http://www.cheshouye.com/api/weizhang/open_task?";
	private String GET_VIOLATION_URL_END = "http://www.cheshouye.com/api/weizhang/close_task?";
	// private static String STRING_REFERER = "http://m.cheshouye.com/weizhang/hebei.html";
	// protected static String strProxyFile = System.getProperty("PROXY_FILE", "https_proxy.txt");
	// protected static ProxyServerManager pMgr = new ProxyServerManager(strProxyFile);
	private CheshouyeParser parser = new CheshouyeParser();

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				strResult = lookupViolation(car, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpHost proxy) {

		String config = ConfigUtils.getCitySourceRole(car.getCityPy(), "CheshouyeService");
		// String config = ServiceConfig.cheShouYeMap.get(car.strCityPy);
		System.out.println("config==========" + config);
		String con[] = config.split(",");
		String STRING_REFERER = con[7];
		System.out.println("STRING_REFERER==========" + STRING_REFERER);
		String result = "";

		try {

			Vector<String> vCookies = new Vector<String>();
			String time = String.valueOf(new Date().getTime());
			String callback = getCallBackStr(time);
			GET_PROVINCE_CONFIG += "callback=" + callback + "&province_id=" + con[3] + "&_=" + (new Long(time) + 1);
			System.out.println("GET_PROVINCE_CONFIG ===========" + GET_PROVINCE_CONFIG);
			String json = "";
			int Lp = 0;
			while (Lp < 3) {
				Lp++;
				String tempRepStr = HttpClientUtil.getURLContentsWithCookies(GET_PROVINCE_CONFIG, vCookies, STRING_REFERER, null);
				System.out.println(" tempRepStr=========" + tempRepStr);

				if (StringUtil.isNotEmpty(tempRepStr)) {
					json = tempRepStr.substring(tempRepStr.indexOf("(") + 1, tempRepStr.length() - 2);
					System.out.println("json ===========" + json);
					break;
				}
			}

			// String

			JSONObject job = JSONObject.parseObject(json);
			String provice_id = job.getString("provice_id");
			String td_key = job.getString("td_key");

			// callback=jQuery1910558003980666399_1417514852294&chepai_no=QZT558&engine_no=771018&city_id=189&car_province_id=14
			// &input_cost=0&vcode=%7B%22cookie_str%22%3A%22%22%2C%22verify_code%22%3A%22%22%2C%22
			// vcode_para%22%3A%7B%22vcode_key%22%3A%22%22%7D%7D
			// &td_key=irii26ci3bcb&car_type=02&_=1417514852296
			// callback:jQuery1910558003980666399_1417514852294
			// chepai_no:QZT558
			// engine_no:771018
			// city_id:189
			// car_province_id:14
			// input_cost:0
			// vcode:{"cookie_str":"","verify_code":"","vcode_para":{"vcode_key":""}}
			// td_key:irii26ci3bcb
			// car_type:02
			// _:1417514852296

			String strBtmId = "";
			String strEngId = "";

			GET_VIOLATION_URL_OPEN += "callback=" + callback + "&car_province_id=" + provice_id + "&city_id=" + con[4] + "&chepai_no=" + "";
			if (new Integer(con[5]) > 0) {
				if (strEngId.length() > new Integer(con[5])) {
					strEngId = strEngId.substring(strEngId.length() - new Integer(con[5]));
				}
				GET_VIOLATION_URL_OPEN += "&engine_no=" + strEngId;
			} else if (new Integer(con[5]) == -1) {
				GET_VIOLATION_URL_OPEN += "&engine_no=" + "";
			}
			if (new Integer(con[6]) > 0) {
				if (strBtmId.length() > new Integer(con[6])) {
					strBtmId = strBtmId.substring(strBtmId.length() - new Integer(con[6]));
				}
				GET_VIOLATION_URL_OPEN += "&chejia_no=" + strBtmId;
			} else if (new Integer(con[6]) == -1) {
				GET_VIOLATION_URL_OPEN += "&chejia_no=" + "";
			}
			GET_VIOLATION_URL_OPEN += "&input_cost=0&vcode=%7B%22cookie_str%22%3A%22%22%2C%22verify_code%22%3A%22%22%2C%22vcode_para%22%3A%7B%22vcode_key%22%3A%22%22%7D%7D" + "&car_type=02&td_key=" + td_key + "&_" + new Date().getTime();
			System.out.println("GET_VIOLATION_URL_OPEN=====" + GET_VIOLATION_URL_OPEN);
			int L = 0;

			while (L < 6) {
				L++;

				try {
					String tempRepStr2 = HttpClientUtil.getURLContentsWithCookies(GET_VIOLATION_URL_OPEN, vCookies, STRING_REFERER, null);
					System.out.println("tempRepStr2==========" + tempRepStr2);
					if (StringUtil.isNotEmpty(tempRepStr2)) {
						String idjson = tempRepStr2.substring(tempRepStr2.indexOf("(") + 1, tempRepStr2.length() - 2);
						JSONObject idjsonjob = JSONObject.parseObject(idjson);
						String id = idjsonjob.getString("id");
						String sign = idjsonjob.getString("sign");

						// close_task?callback=jQuery1910558003980666399_1417514852294&id=15462938&sign=A4AC9384BD63E3206931A145C905416C&_=1417514852297
						GET_VIOLATION_URL_END += "callback=" + callback + "&id=" + id + "&sign=" + sign + "&_" + new Date().getTime();
						System.out.println(GET_VIOLATION_URL_END);
						int Loop = 0;
						while (Loop < 15) {
							Loop++;
							String strRep = HttpClientUtil.getURLContentsWithCookies(GET_VIOLATION_URL_END, vCookies, STRING_REFERER, null);
							// jQuery19106359062993433326_1421632558332({"id":27317636,"sign":"9B2944D20B908F924F0735C0FD605DB2"});
							// System.out.println("strRep========"+strRep);
							if (strRep.contains("\"status\":2000")) {
								LogUtil.doMkLogData_cheshouye(strRep, car, "ok");
								return ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
							}
							if (strRep.contains("\"status\":2001")) {
								LogUtil.doMkLogData_cheshouye(strRep, car, "ok");
								strRep = strRep.substring(strRep.indexOf("(") + 1, strRep.length() - 2);
								return parser.parse(strRep, car);
							}

							Thread.sleep(2000);

						}
					} else {
						continue;
					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}

		} catch (Exception e) {
			LogUtil.doMkLogData_cheshouye("excepton", car, "err");
			e.printStackTrace();
		}
		if ("".equals(result)) {
			LogUtil.doMkLogData_cheshouye("", car, "err");
		}
		return result;
	}

	public void main2() throws Exception {
		String STRING_REFERER = "http://m.cheshouye.com/weizhang/hebei.html";
		// System.out.println(new Date().getTime());
		String time = String.valueOf(new Date().getTime());
		Vector<String> vCookies = new Vector<String>();
		String callback = getCallBackStr(time);
		GET_PROVINCE_CONFIG += "callback=" + callback + "&province_id=17&_=" + (new Long(time) + 1);
		System.out.println(GET_PROVINCE_CONFIG);
		String tempRepStr = HttpClientUtil.getURLContentsWithCookies(GET_PROVINCE_CONFIG, vCookies, STRING_REFERER, null);
		String json = tempRepStr.substring(tempRepStr.indexOf("(") + 1, tempRepStr.length() - 2);
		try {
			JSONObject job = JSONObject.parseObject(json);
			String provice_id = job.getString("provice_id");
			String td_key = job.getString("td_key");
			// callback=jQuery1910558003980666399_1417514852294&chepai_no=QZT558&engine_no=771018&city_id=189&car_province_id=14
			// &input_cost=0&vcode=%7B%22cookie_str%22%3A%22%22%2C%22verify_code%22%3A%22%22%2C%22
			// vcode_para%22%3A%7B%22vcode_key%22%3A%22%22%7D%7D
			// &td_key=irii26ci3bcb&car_type=02&_=1417514852296
			// callback:jQuery1910558003980666399_1417514852294
			// chepai_no:QZT558
			// engine_no:771018
			// city_id:189
			// car_province_id:14
			// input_cost:0
			// vcode:{"cookie_str":"","verify_code":"","vcode_para":{"vcode_key":""}}
			// td_key:irii26ci3bcb
			// car_type:02
			// _:1417514852296

			GET_VIOLATION_URL_OPEN += "callback=" + callback + "&car_province_id=" + provice_id + "&city_id=213&chepai_no=BQ1310&chejia_no=2433" + "&input_cost=0&vcode=%7B%22cookie_str%22%3A%22%22%2C%22verify_code%22%3A%22%22%2C%22vcode_para%22%3A%7B%22vcode_key%22%3A%22%22%7D%7D" + "&car_type=02&td_key=" + td_key + "&_" + new Date().getTime();
			System.out.println("GET_VIOLATION_URL_OPEN=====" + GET_VIOLATION_URL_OPEN);
			String tempRepStr2 = HttpClientUtil.getURLContentsWithCookies(GET_VIOLATION_URL_OPEN, vCookies, STRING_REFERER, null);
			System.out.println(tempRepStr2);
			String idjson = tempRepStr2.substring(tempRepStr2.indexOf("(") + 1, tempRepStr2.length() - 2);
			JSONObject idjsonjob = JSONObject.parseObject(idjson);
			String id = idjsonjob.getString("id");
			String sign = idjsonjob.getString("sign");

			// close_task?callback=jQuery1910558003980666399_1417514852294&id=15462938&sign=A4AC9384BD63E3206931A145C905416C&_=1417514852297
			GET_VIOLATION_URL_END += "callback=" + callback + "&id=" + id + "&sign=" + sign + "&_" + new Date().getTime();
			System.out.println(GET_VIOLATION_URL_END);
			int Loop = 0;
			while (Loop < 15) {
				Loop++;
				String strRep = HttpClientUtil.getURLContentsWithCookies(GET_VIOLATION_URL_END, vCookies, STRING_REFERER, null);
				System.out.println("strRep========" + strRep);
				if (strRep.contains("\"status\":2001") || strRep.contains("\"status\":2000")) {
					break;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public static String getCallBackStr(String time) {
		String ret = "jQuery";
		for (int i = 0; i < 20; i++) {
			int a = (int) (Math.random() * 9);
			if (i == 0) {
				a = 1;
			}
			ret += a;
		}
		return ret + "_" + time;
	}
}
