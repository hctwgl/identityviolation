package com.mapbar.city.todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Vector;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.base.DriverCase;
import com.mapbar.traffic.score.base.DriverProfile;
import com.mapbar.traffic.score.base.ResultCache;
import com.mapbar.traffic.score.logs.LogUtil;
import com.mapbar.traffic.score.transfer.factory.Transfer;
import com.mapbar.traffic.score.utils.HttpsProxy;
import com.mapbar.traffic.score.utils.HttpsUtils;
import com.mapbar.traffic.score.utils.StringUtil;

public class CitySuqianService implements Transfer {
	private static final String GET_VIOLATION_URL = "http://www.sqjg.net/Jgcx/";
	private static final String STRING_REFERER = "http://www.sqjg.net/Jgcx/";

	// http://www.sqjg.net/Jgcx/
	// http://www.sqjg.net/Jgcx/?__msg=14%2C3%2C31%3BJgcx.VehDetailxhr4%2C7%2C2%2C4%2C1%2C1%3Bargs%E8%8B%8FNXB322026722**
	// http://www.sqjg.net/Jgcx/?__msg=11%2C3%2C19%3BJgcx.getVioxhr4%2C7%2C2%3Bargs%E8%8B%8FNXB32202
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

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = "";

		Vector<String> vCookies = new Vector<String>();

		String engId = car.getCityPy();
		if (engId.length() > 4) {
			engId = engId.substring(engId.length() - 4, engId.length());
		} else if (engId.length() < 4) {
			return ResultCache.toErrJsonResult("车辆信息错误,请输入发动机号后六位！");
		}

		int Loop = 0;
		while (Loop < 2) {
			Loop++;
			try {
				 HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next, "UTF-8");
				// String refStr = NetHelper.getURLContentsWithCookies(refer, vCookies, STRING_REFERER1, next, "UTF-8");
				// System.out.println(refStr);
				// System.out.println(indexHtml);

				String checkDate = "__msg=" + URLEncoder.encode("14,3,31;Jgcx.VehDetailxhr4,7,2,4,1,1;args" + car.getCityPy(), "UTF-8") + car.getCityPy() + "02" + engId + "**";

				String checkResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, checkDate, vCookies, STRING_REFERER, "UTF-8", next);
				// System.out.println(checkResp);
				if (StringUtil.isNotEmpty(checkResp) && checkResp.contains("违法未处理")) {
					String postDate = "__msg=" + URLEncoder.encode("11,3,19;Jgcx.getVioxhr4,7,2;args" + car.getCityPy() + car.getCityPy() + "02", "UTF-8");
					// String postDate = "hpno="+URLEncoder.encode(car.strState, "UTF-8")+"&hphm="+car.strRegId+"&hpzl=02&clsbdh="+engId;

					String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "UTF-8", next);
					// ;response=[['在高速公路或城市快速路以外的道路上行驶时，驾驶人未按规定使用安全带的','2015年08月01日08时03分','沂河北堤颜集街路口','3213227902492983','沭阳大队','2015年09月17日','闯红灯','6011','','50']]
					// System.out.println(strResp);
					strResp = strResp.replace(";response=", "");
					strResp = strResp.substring(1, strResp.length() - 1);
					System.out.println(strResp);
					String str[] = strResp.split("\\],\\[");

					DriverCase vc = new DriverCase();
					JSONArray jcases = new JSONArray();
					JSONObject jcase = null;
					int i = 0;
					for (String s : str) {
						i++;
						s = s.replace("[", "").replace("]", "");
						System.out.println(s);
						String data[] = s.split("','");

						String con = data[0].replace("'", "");
						String dizhi = data[2];
						String time = data[1].replace("年", "-").replace("月", "-").replace("日", " ");
						time = time.replace("时", ":").replace("分", ":00");

						String money = data[9].replace("'", "");
						if ("null".equals(money)) {
							money = "未知";
						} else if ("".equals(money)) {
							money = "0";
						}

						String jifen = data[8];
						if ("null".equals(jifen)) {
							jifen = "未知";
						} else if ("".equals(jifen)) {
							jifen = "0";
						}

						jcase = new JSONObject();
						jcase.put("序号", String.valueOf(i));
						jcase.put("处理情况", "未处理");

						jcase.put("违法时间", time);
						jcase.put("违法地点", dizhi);
						jcase.put("违法内容", con);
						jcase.put("记分", jifen);
						jcase.put("罚款", money);
						if (jcase != null) {
							jcases.add(jcase);
						}
					}
					if (jcases.size() > 0) {
						vc.json.put("data", jcases);

						vc.json.put("count", jcases.size());

						vc.json.put("status", "ok");

						ret = vc.toString();
					}
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					break;
				} else if (StringUtil.isNotEmpty(checkResp) && checkResp.contains("发动机号后4位错误")) {
					LogUtil.doMkLogData_JGU_With_Msg(car, "err", "信息输入错误");
					ret = ResultCache.toErrJsonResult("车辆信息错误,发动机号错误！");
					break;
				} else {
					LogUtil.doMkLogData_JGU_With_Msg(car, "ok", "");
					ret = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					break;
				}

			} catch (SocketTimeoutException e) {
				LogUtil.doMkLogData_JGU_With_Msg(car, "err", "网络连接超时");
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				LogUtil.doMkLogData_JGU_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.doMkLogData_JGU_With_Msg(car, "err", "网络连接异常");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.doMkLogData_JGU_With_Msg(car, "err", "其他错误");
				e.printStackTrace();
			}

		}

		if ("".equals(ret)) {
			LogUtil.doMkLogData_JGU_With_Msg(car, "err", "其他错误");
		}
		return ret;
	}

	/**
	 * [ '在划有停车泊位的路段，在停车泊位外停放的', '2015年03月24日16时30分', '青岛路与上海路', '3213227902263178', '一中队', '2015年09月17日', '其它设备', '70052', '', '50' ], [ '机动车违反禁止标线指示的', '2015年04月01日09时40分', '章集柴米河桥', '3213227902280523', '沭阳大队', '2015年09月17日', '其它设备', '13451', '3', '100' ], [ '机动车违反禁止标线指示的', '2015年04月01日11时15分', '章集柴米河桥', '3213227902280640', '沭阳大队', '2015年09月17日', '其它设备', '13451', '3', '100' ], [ '驾驶中型以上载客载货汽车、校车、危险物品运输车辆以外的其他机动车行驶超过规定时速20%以上未达到50%的',
	 * '2015年04月03日15时54分', '沭阳县沭李路22公里', '3213227902284109', '321322005900', '2015年09月17日', '测速设备', '1636', '6', '200' ]
	 */
}
