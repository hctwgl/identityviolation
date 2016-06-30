package com.mapbar.driver.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class YzmParser {

	public static String imageDataType_url = "url";
	public static String imageDataType_assert = "assert";
	public static String imageDataType_drawable = "drawable";
	public static String imageDataType_data = "data";

	public static String refresh_status = "999";
	public static String yzm_err_status = "90";
	public static String yzm_init_status = "99";
	// refresh_yzm_url
	public static String refresh_image_url = ConfigUtil.bundle.getString("refresh_yzm_url");

	public static String yzm_submit_url = ConfigUtil.bundle.getString("yzm_submit_url");

	public static String refresh_logo_url = "http://r.mapbar.com/diejiaceng/images/btn_refresh.png";

	public static String yzmparser(String json) {
		String ret = "";
		try {
			JSONObject jcases = JSONObject.parseObject(json);
			String status = jcases.getString("status");
			if (status.equals(yzm_init_status)) {
				ret = yzmparserForInit(jcases, null);
			} else if (status.equals(refresh_status)) {
				ret = yzmparserForFresh(jcases, "请根据图片输入验证码");
			} else if (status.equals(yzm_err_status)) {
				ret = yzmparserForFresh(jcases, "图片验证码输入错误，请重新输入。");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private static String yzmparserForFresh(JSONObject job, String message) {
		JSONObject jcases = new JSONObject();
		try {
			jcases.put("status", "9");
			jcases.put("msg", "该城市查询需要支持验证码，如未弹出验证码图片，请更新系统到最新版本，给您带来的不变请您谅解，谢谢!");

			JSONObject ob1 = new JSONObject();
			jcases.put("extra_expand_info", ob1);

			ob1.put("type", "update");
			ob1.put("message", message);

			/** 包装actions start **/
			JSONArray actions = new JSONArray();
			ob1.put("action", actions);

			JSONObject act1 = new JSONObject();
			actions.add(act1);
			act1.put("id", "refresh");
			act1.put("type", "request");

			JSONArray act1params = new JSONArray();
			act1.put("urlParams", act1params);
			JSONObject act1param1 = new JSONObject();
			act1param1.put("paramNam1", "sid");
			act1param1.put("paramValue", job.getString("sid"));
			act1params.add(act1param1);

			JSONObject act1param2 = new JSONObject();
			act1param2.put("paramName", "cs");
			act1param2.put("paramValue", job.getString("cs"));
			act1params.add(act1param2);
			/** 包装actions end **/

			/** 包装 content start **/
			JSONArray contents = new JSONArray();
			ob1.put("content", contents);

			JSONObject con1 = new JSONObject();
			contents.add(con1);
			con1.put("id", "vcodeImage");
			con1.put("row", 1);
			con1.put("type", "resize_image");
			con1.put("scaleType", "CENTER");
			con1.put("width", job.getInteger("width"));
			con1.put("height", job.getInteger("height"));
			con1.put("maxWidth", 80);
			con1.put("maxHeight", 80);
			con1.put("minWidth", 10);
			con1.put("minHeight", 10);
			con1.put("weight", 1);
			con1.put("gravity", "CENTER_VERTICAL");
			con1.put("actionId", "refresh");

			JSONObject con1src = new JSONObject();
			con1.put("src", con1src);
			con1src.put("provideWay", "data");
			con1src.put("source", job.getString("source"));
			con1src.put("width", job.getInteger("width"));
			con1src.put("height", job.getInteger("height"));
			/** 包装 content end **/
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jcases.toString();
	}

	private static String yzmparserForInit(JSONObject job, String title) {
		JSONObject jcases = new JSONObject();
		try {
			jcases.put("status", "9");
			jcases.put("msg", "该城市查询需要支持验证码，如未弹出验证码图片，请更新系统到最新版本，给您带来的不变请您谅解，谢谢!");

			JSONObject ob1 = new JSONObject();
			jcases.put("extra_expand_info", ob1);

			ob1.put("title", "图吧提示");
			ob1.put("type", "dialog");
			ob1.put("message", "请根据图片输入验证码");

			/** 包装actions start **/
			JSONArray actions = new JSONArray();
			ob1.put("action", actions);

			JSONObject act1 = new JSONObject();
			actions.add(act1);
			act1.put("id", "refresh");
			act1.put("type", "request");
			act1.put("url", refresh_image_url);

			JSONArray act1params = new JSONArray();
			act1.put("urlParams", act1params);
			JSONObject act1param1 = new JSONObject();
			act1param1.put("paramNam1", "sid");
			act1param1.put("paramValue", job.getString("sid"));
			act1params.add(act1param1);

			JSONObject act1param2 = new JSONObject();
			act1param2.put("paramName", "cs");
			act1param2.put("paramValue", job.getString("cs"));
			act1params.add(act1param2);

			JSONObject act2 = new JSONObject();
			actions.add(act2);
			act2.put("id", "submit");
			act2.put("type", "request");
			act2.put("url", yzm_submit_url);
			act2.put("originallyParams", true);

			JSONArray act2params = new JSONArray();
			act2.put("urlParams", act2params);
			JSONObject act2param1 = new JSONObject();
			act2param1.put("paramNam1", "sid");
			act2param1.put("paramValue", job.getString("sid"));
			act2params.add(act2param1);

			JSONObject act2param2 = new JSONObject();
			act2param2.put("paramName", "cs");
			act2param2.put("paramValue", job.getString("cs"));
			act2params.add(act2param2);

			JSONObject act2param3 = new JSONObject();
			act2param3.put("paramName", "vcode");
			act2param3.put("paramValueId", "vcodeInput");
			act2params.add(act2param3);

			JSONObject act3 = new JSONObject();
			actions.add(act3);
			act3.put("id", "cancel");
			act3.put("type", "cancel");

			/** 包装actions end **/

			/** 包装 content start **/
			JSONArray contents = new JSONArray();
			ob1.put("content", contents);

			JSONObject con1 = new JSONObject();
			contents.add(con1);
			con1.put("id", "vcodeImage");
			con1.put("row", 1);
			con1.put("type", "resize_image");
			con1.put("scaleType", "CENTER");
			con1.put("width", job.getInteger("width"));
			con1.put("height", job.getInteger("height"));
			con1.put("maxWidth", 80);
			con1.put("maxHeight", 80);
			con1.put("minWidth", 10);
			con1.put("minHeight", 10);
			con1.put("weight", 1);
			con1.put("gravity", "CENTER_VERTICAL");
			con1.put("actionId", "refresh");

			JSONObject con1src = new JSONObject();
			con1.put("src", con1src);
			con1src.put("provideWay", "data");
			con1src.put("source", job.getString("source"));
			con1src.put("width", job.getInteger("width"));
			con1src.put("height", job.getInteger("height"));

			JSONObject con2 = new JSONObject();
			contents.add(con2);

			con2.put("id", "refreshButton");
			con2.put("row", 1);
			con2.put("type", "image");
			con2.put("actionId", "refresh");
			JSONObject con2src = new JSONObject();
			con2.put("src", con2src);
			con2src.put("provideWay", "url");
			con2src.put("source", refresh_logo_url);
			con2src.put("width", 44);
			con2src.put("height", 45);

			JSONObject con3 = new JSONObject();
			contents.add(con3);

			con3.put("id", "vcodeInput");
			con3.put("row", 2);
			con3.put("type", "edit");
			con3.put("lable", "请输入验证码...");
			/** 包装 content end **/

			JSONObject confirmButton = new JSONObject();
			confirmButton.put("lable", "提交");
			confirmButton.put("actionId", "submit");
			ob1.put("confirmButton", confirmButton);

			JSONObject cancleButton = new JSONObject();
			cancleButton.put("lable", "取消");
			cancleButton.put("actionId", "cancel");
			ob1.put("cancelButton", cancleButton);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jcases.toString();
	}

}
