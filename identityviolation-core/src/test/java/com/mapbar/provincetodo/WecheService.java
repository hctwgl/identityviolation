package com.mapbar.provincetodo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Hashtable;
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

public class WecheService implements Transfer {
	// private static final String VALIDATE_IMAGE_URL="http://www.stc.gov.cn:8082/szwsjj_web/ImgServlet.action?rnd=";

	private static final String GET_VIOLATION_URL = "http://cha.weiche.me/front/do-index.php";
	private static final String STRING_REFERER = "http://cha.weiche.me/uc";
	// private static final String IMAGE_TYPE="jpg";
	// private static String CHK_URL = "http://chaxun.wcar.net.cn/";
	private static Hashtable<String, String> htKeys = new Hashtable<String, String>();
	static {
		htKeys.put("违章时间", "违法时间");
		htKeys.put("违章地点", "违法地点");
		htKeys.put("违章行为", "违法内容");
	}

	@Override
	public String checkDriverScore(DriverProfile car) {
		String strResult = "";
		try {
			if (car != null && StringUtil.isNotEmpty(car.getCityPy())) {
				// strResult = lookupViolation(car, ProxyManager.next(true));
				strResult = lookupViolation(car, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	private String lookupViolation(DriverProfile car, HttpsProxy next) {
		String ret = "";
		try {

			int Loop = 0;
			while (Loop < 3) {
				Vector<String> vCookies = new Vector<String>();

			HttpsUtils.getURLContentsWithCookies(STRING_REFERER, vCookies, STRING_REFERER, next);
				System.out.println(vCookies);
				Loop++;
				String postDate = car.toWecheString() + "&vcode_num=&c=uc&mobile_num=" + URLEncoder.encode("请输入手机号码", "UTF-8");
				// System.out.println(postDate);

				String strResp = HttpsUtils.postURLContentsWithCookies(GET_VIOLATION_URL, postDate, vCookies, STRING_REFERER, "utf-8", next);
				// province=北京&pinyin=beijing&car_province=京&license_plate_num=PC3U67&body_num=选填&engine_num=076078&c=uc&mobile_num=请输入手机号码
				// pinyin=beijing&province=%E5%8C%97%E4%BA%AC&license_plate_num=PC3U67&car_province=%E4%BA%AC&engine_num=076078&mobile_num=%E8%AF%B7%E8%BE%93%E5%85%A5%E6%89%8B%E6%9C%BA%E5%8F%B7%E7%A0%81&body_num=%E9%80%89%E5%A1%AB&vcode_num=&c=uc
				// province=%E5%8C%97%E4%BA%AC&pinyin=beijing&car_province=%E4%BA%AC&license_plate_num=PC3U67&body_num=%E9%80%89%E5%A1%AB&engine_num=076078&c=uc&mobile_num=%E8%AF%B7%E8%BE%93%E5%85%A5%E6%89%8B%E6%9C%BA%E5%8F%B7%E7%A0%81
				// System.out.println("strResp="+strResp);
				// {"vehicle_status":"ok","no":4,"totalpoint":9,"totalfine":400,"html":"<table cellspacing=\"0\" cellpadding=\"0\" ><thead><tr><td width=\"12%\">\u5e8f\u53f7<\/td><td width=\"15%\">\u8fdd\u7ae0\u65f6\u95f4<\/td><td >\u8fdd\u7ae0\u5730\u70b9<\/td><td>\u8fdd\u7ae0\u884c\u4e3a<\/td><td width=\"7%\" >\u8bb0\u5206<\/td><td width=\"7%\" >\u7f5a\u6b3e<\/td><td width=\"13%\"
				// >\u5904\u7406\u60c5\u51b5<\/td><\/tr><\/thead><tbody><tr><td>1<\/td><td>2014-07-29
				// 08:21:00<\/td><td>\u5317\u4eac\u5e02\u4e30\u53f0\u533a\u897f\u7ad9\u5317\u5e7f\u573a\u897f\u7ad9\u5317\u5e7f\u573a\u4e1c\u4e00\u53e3\u81f3\u897f\u7ad9\u5317\u5e7f\u573a\u5761\u9053\u4e1c\u53e3\u6bb5<\/td><td>\u8fdd\u53cd\u7981\u6b62\u505c\u8f66\u6807\u5fd7\u6307\u793a\u7684<\/td><td>3\u5206<\/td><td>100<\/td><td>\u672a\u5904\u7406<\/td><\/tr><tr><td>2<\/td><td>2014-07-10
				// 13:06:00<\/td><td>\u5317\u4eac\u5e02\u4e30\u53f0\u533a\u6d0b\u6865\u5317 \u7531\u5357\u5411\u5317<\/td><td>\u672a\u6309\u5c3e\u53f7\u9650\u5236\u901a\u884c\u7684<\/td><td>0\u5206<\/td><td>100<\/td><td>\u672a\u5904\u7406<\/td><\/tr><tr><td>3<\/td><td>2014-04-13
				// 16:25:00<\/td><td>\u5c71\u4e1c\u7701\u5fb7\u5dde\u5e02\u8fd0\u7ba1\u6240\u8def\u53e3\uff08\u5b81\u5fb7\u8def\u4e0e\u9633\u5149\u5927\u8857\u5341\u5b57\u8def\u53e3\uff09<\/td><td>\u8fdd\u53cd\u7981\u6b62\u6807\u7ebf\u6307\u793a\u7684<\/td><td>3\u5206<\/td><td>100<\/td><td>\u672a\u5904\u7406<\/td><\/tr><tr><td>4<\/td><td>2014-02-14
				// 14:35:00<\/td><td>\u5c71\u4e1c\u7701\u5fb7\u5dde\u5e02\u5357\u73af\u8def<\/td><td>\u8fdd\u53cd\u7981\u6b62\u6807\u7ebf\u6307\u793a\u7684<\/td><td>3\u5206<\/td><td>100<\/td><td>\u672a\u5904\u7406<\/td><\/tr><\/tbody><\/table>","time":"01.19 17:57"}
				// {"vehicle_status":"unknown","no":0,"totalpoint":0,"totalfine":0,"html":"<img class=\"no_violation\" src=\"http:\/\/img.buding.cn\/weiche\/2014\/09\/28\/afa1a8f178364e991be0a5a91a143c6a.png\" alt=\"\u8d5e\">","time":"01.19 17:58"}
				// {"vehicle_status":"ok","no":3,"totalpoint":0,"totalfine":600,"html":"<div class='oneres'><div class='fit-width resault'><p class='violation_type'>\u673a\u52a8\u8f66\u8fdd\u89c4\u4f7f\u7528\u4e13\u7528\u8f66\u9053<\/p><p class='time'>2014.06.24 \u661f\u671f\u4e8c 15:51<\/p><p class='address'>\u5317\u4eac\u5e02\u4e1c\u57ce\u533a\u4e1c\u957f\u5b89\u8857\u5357\u6cb3\u6cbf\u8def\u53e3\u897f \u4e1c\u5411\u897f<\/p><p><span
				// class='kf'>\u6263\u5206<\/span><b class='point'>0\u5206<\/b><span class='fk'>\u7f5a\u6b3e<\/span><b class='fine'>200\u5143<\/b><\/p><\/div><\/div><div class='oneres'><div class='fit-width resault'><p class='violation_type'>\u4e0d\u6309\u89c4\u5b9a\u505c\u8f66<\/p><p class='time'>2013.11.09 \u661f\u671f\u516d 08:46<\/p><p
				// class='address'>\u5317\u4eac\u5e02\u897f\u57ce\u533a\u5357\u6a2a\u897f\u8857\u4e03\u4e95\u80e1\u5468\u5357\u53e3\u81f3\u76c6\u513f\u80e1\u540c\u5317\u53e3\u6bb5<\/p><p><span class='kf'>\u6263\u5206<\/span><b class='point'>0\u5206<\/b><span class='fk'>\u7f5a\u6b3e<\/span><b class='fine'>200\u5143<\/b><\/p><\/div><\/div><div class='oneres'><div class='fit-width resault'><p
				// class='violation_type'>\u4e0d\u6309\u89c4\u5b9a\u505c\u8f66<\/p><p class='time'>2013.01.26 \u661f\u671f\u516d 09:28<\/p><p class='address'>\u5317\u4eac\u5e02\u897f\u57ce\u533a\u5ba3\u6b66\u95e8\u4e1c\u5927\u8857\u548c\u5e73\u95e8\u81f3\u5ba3\u6b66\u95e8\u6bb5<\/p><p><span class='kf'>\u6263\u5206<\/span><b class='point'>0\u5206<\/b><span class='fk'>\u7f5a\u6b3e<\/span><b
				// class='fine'>200\u5143<\/b><\/p><\/div><\/div>","time":"05.22 16:02"}
				System.out.println(strResp);
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("engine number error")) {
					LogUtil.doMkLogData_wecar(car, "err", "信息输入错误,发动机号有误");
					return ResultCache.toErrJsonResult("车辆信息错误,发动机号有误,请按照要求填写正确的发动机号，谢谢！");
				}
				if (StringUtil.isNotEmpty(strResp) && strResp.contains("body number error")) {
					LogUtil.doMkLogData_wecar(car, "err", "信息输入错误,车架号有误");
					return ResultCache.toErrJsonResult("车辆信息错误,车架号有误,请按照要求填写正确的车架号，谢谢！");
				}

				if (StringUtil.isNotEmpty(strResp) && (strResp.contains("vehicle_status\":\"ok\",\"no\":0") || strResp.contains("vehicle_status\":\"error\",\"no\":0"))) {
					LogUtil.doMkLogData_wecar(car, "ok", strResp);
					strResp = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");
					return strResp;
				} else if (StringUtil.isNotEmpty(strResp) && strResp.contains("\"vehicle_status\":\"ok\"")) {
					JSONObject jresp = JSONObject.parseObject(strResp);
					LogUtil.doMkLogData_wecar(jresp.toString(), car);
					LogUtil.doMkLogData_wecar(car, "ok", strResp);
					ret = parseData(jresp);
					break;
				}
			}

		} catch (SocketTimeoutException e) {
			LogUtil.doMkLogData_wecar(car, "err", "网络连接超时");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			LogUtil.doMkLogData_wecar(car, "err", "网络连接异常");
			e.printStackTrace();
		} catch (IOException e) {
			LogUtil.doMkLogData_wecar(car, "err", "网络连接异常");
			e.printStackTrace();
		} catch (Exception e) {
			LogUtil.doMkLogData_wecar(car, "err", "其他错误");
			e.printStackTrace();
		}
		if ("".equals(ret)) {
			LogUtil.doMkLogData_wecar(car, "err", "其他错误");
		}
		return ret;
	}

	private static String parseData(JSONObject jresp) {
		String strResult = ResultCache.toOkJsonResult("恭喜你，没有违章信息!");

		try {

			if (jresp.containsKey("violations") && jresp.getJSONArray("violations").size() > 0) {
				try {
					DriverCase vc = new DriverCase();

					JSONArray jcases = new JSONArray();
					JSONObject jcase = null;

					JSONArray array = jresp.getJSONArray("violations");
					for (int i = 0; i < array.size(); i++) {
						JSONObject data = array.getJSONObject(i);
						String address = data.getString("address");
						address = address.replace("\u2015", "—");
						String time = data.getString("time");
						String content = data.getString("violation_type");
						String fen = data.getString("point");
						String money = String.valueOf(data.getString("fine"));
						int deal = data.getInteger("handled");
						jcase = new JSONObject();
						jcase.put("序号", String.valueOf(i + 1));
						if (deal == 0) {
							jcase.put("处理情况", "未处理");
						} else {
							jcase.put("处理情况", "已处理");
						}

						jcase.put("违法时间", time);
						jcase.put("违法地点", address);
						jcase.put("违法内容", content);
						jcase.put("记分", fen.replace("分", ""));
						jcase.put("罚款", money);
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

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResult;
	}

}
