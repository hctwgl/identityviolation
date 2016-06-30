package com.mapbar.traffic.score.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;

import com.alibaba.fastjson.JSONObject;

public class DateUtils {

	public static final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
	public static final FastDateFormat format = FastDateFormat.getInstance("MM-dd");
	public static final FastDateFormat numFormat = FastDateFormat.getInstance("yyyyMMdd");

	public static Integer getCurrentDate() {
		Integer time = null;
		try {
			String date = numFormat.format(new Date());
			time = new Integer(date);
		} catch (Exception e) {
		}
		return time;
	}

	public static String getDateByFormat(String format) {
		return getDateByFormat(format, new Date());
	}

	public static String getDateByFormat(String format, Date date) {
		String time = null;
		try {
			DateFormat df = new SimpleDateFormat(format);
			time = df.format(date);
		} catch (Exception e) {
		}
		return time;
	}

	public static String getCurrentDateStr() {
		String time = null;
		try {
			time = dateFormat.format(new Date());
		} catch (Exception e) {
		}
		return time;
	}

	public static String getPreDate(int amount, int field) {
		String time = null;
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.YEAR, amount);
			time = dateFormat.format(c.getTime());
		} catch (Exception e) {
		}
		return time;
	}

	public static String qingFensj(JSONObject jobj) {
		String ret = "未知";
		try {
			if (!jobj.isEmpty()) {
				String lissuedate = jobj.getString("lissuedate");
				String tjrq = jobj.getString("tjrq");
				long timeNow = format.parse(format.format(new Date())).getTime();
				Calendar cal = Calendar.getInstance();
				if (StringUtil.isNotEmpty(lissuedate)) {
					String md = format.format(DateUtils.addDay(dateFormat.parse(lissuedate), -1));
					Date date = format.parse(md);
					long time = date.getTime();
					if (time <= timeNow) {
						ret = cal.get(Calendar.YEAR) + 1 + "-" + md;
					} else {
						ret = cal.get(Calendar.YEAR) + "-" + md;
					}
				} else if (StringUtil.isNotEmpty(tjrq)) {
					String md = format.format(DateUtils.addDay(dateFormat.parse(tjrq), -1));
					Date date = format.parse(md);
					long time = date.getTime();
					if (time <= timeNow) {
						ret = cal.get(Calendar.YEAR) + 1 + "-" + md;
					} else {
						ret = cal.get(Calendar.YEAR) + "-" + md;
					}
				} else {
					ret = "未知";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 日期增加天数
	 * 
	 * @param d
	 * @param days
	 * @return
	 */
	public static Date addDay(Date d, int days) {
		Calendar cal = Calendar.getInstance(new Locale("zh-CN"));
		cal.setTime(d);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
}
