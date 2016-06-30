package com.mapbar.traffic.score.base;

import java.util.Hashtable;

import com.alibaba.fastjson.JSONObject;
import com.mapbar.traffic.score.utils.StringUtil;

public class ResultCache {
	public static Hashtable<String, MsgBag> htCache = new Hashtable<String, MsgBag>();

	public static void clear(String strKey) {
		htCache.remove(strKey);
	}

	public static void clearAll() {
		htCache.clear();
	}

	public static String check(String strKey) {
		String strMsg = null;
		try {
			if (htCache.containsKey(strKey)) {
				MsgBag msg = htCache.get(strKey);

				if (msg != null) {
					if (!msg.isExpired()) {
						strMsg = msg.strResult;
					} else {
						htCache.remove(strKey);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strMsg;
	}

	public static void put(String strKey, String strMsg, long timestamp) {
		try {
			MsgBag msg = htCache.get(strKey);

			if (msg == null) {
				msg = new MsgBag(strMsg);
				htCache.put(strKey, msg);
			}

			if (msg != null) {
				msg.update(strMsg, timestamp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String toOkJsonResult(String strMsg) {
		String strJson = null;

		try {
			JSONObject json = new JSONObject();
			json.put("status", "ok");

			if (StringUtil.isNotEmpty(strMsg)) {
				json.put("msg", strMsg);
			}
			strJson = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strJson;
	}

	public static String toErrJsonResult(String strMsg) {
		String strJson = null;

		try {
			JSONObject json = new JSONObject();
			json.put("status", "err");

			if (StringUtil.isNotEmpty(strMsg)) {
				json.put("msg", strMsg);
			} else {
				json.put("msg", "服务器繁忙，请稍后再试.");
			}

			strJson = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strJson;
	}

	public static String toJsonResult(String strMsg, String strStatus) {
		String strJson = null;

		try {
			JSONObject json = new JSONObject();

			if (!StringUtil.isNotEmpty(strStatus))
				strStatus = "ok";

			if (StringUtil.isNotEmpty(strMsg)) {
				json.put("msg", strMsg);
			} else {
				strStatus = "err";
				json.put("msg", "服务维护中, 请稍候再试.");
			}

			json.put("status", strStatus);

			strJson = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strJson;
	}

	static class MsgBag {
		long timestamp = 0;

		String strResult = null;

		MsgBag(String strResult) {
			update(strResult, System.currentTimeMillis());
		}

		void update(String strResult, long timestamp) {
			this.timestamp = timestamp;
			this.strResult = strResult;
		}

		boolean isExpired() {
			// 1 hour
			return (System.currentTimeMillis() - this.timestamp) > 3600000 * 24;
		}
	}

}
