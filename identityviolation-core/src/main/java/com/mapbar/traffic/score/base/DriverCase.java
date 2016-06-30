package com.mapbar.traffic.score.base;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

public class DriverCase implements Serializable{
	private static final long serialVersionUID = 3852053295798118303L;
	public static String KEY_CASES = "vcases";
	public static String KEY_UPDATED = "updated";

	public JSONObject json = new JSONObject();

	public Date updated = new Date();

	public String toString() {
		return this.json.toString();
	}
}
