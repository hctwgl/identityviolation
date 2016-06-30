package com.mapbar.driver.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * <p>
 * 日期时间工具类，提供对日期和时间进行各种运算操作的支持
 * </p>
 */
public class DateUtil {

	private DateUtil() {
	}

	public static final String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyyMMdd", "yyyyMMdd HH:mm:ss", "yyyyMMdd HH:mm", "yyyyMM", "yyyy年MM月dd日", "yyyy年MM月dd日 HH时:mm分:ss秒", "yyyy年MM月dd日 HH时:mm分", "yyyy年MM月", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };
	public static final String timeFormatStr = "yyyy-MM-dd HH:mm:ss";
	public static final String numFormatStr = "yyyyMMddHHmmss";
	public static final String dateFormatStr = "yyyy-MM-dd";
	public static final String mimuteFormatStr = "yyyy-MM-dd HH:mm";
	public static final String todayBeginFormatStr = "yyyy-MM-dd 00:00:00";
	public static final String todayEndFormatStr = "yyyy-MM-dd 23:59:59";
	/**
	 * 1000毫秒*60秒*60分*24小时=一天毫秒数
	 */
	public static final long MILLISECOND_DAY = 86400000;

	/**
	 * 1年=31536000秒
	 */
	public static final int SECOND_YEAR = 31536000;
	/**
	 * 纯数字、带毫秒的日期格式串
	 */
	public static final String numMillFormatStr = "yyyyMMddHHmmssSSS";

	/** 存放不同的日期模板格式的sdf的Map */
	public static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	/** 锁对象 */
	// private static final Object lockObj = new Object();

	/**
	 * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
	 * 
	 * @param pattern
	 * @return
	 */
	// private static SimpleDateFormat getSdf(final String pattern) {
	// ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
	// // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
	// if (tl == null) {
	// synchronized (lockObj) {
	// tl = sdfMap.get(pattern);
	// if (tl == null) {
	// // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
	// // System.out.println("put new sdf of pattern " + pattern + " to map");
	// // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
	// tl = new ThreadLocal<SimpleDateFormat>() {
	// @Override
	// protected SimpleDateFormat initialValue() {
	// // System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
	// return new SimpleDateFormat(pattern);
	// }
	// };
	// sdfMap.put(pattern, tl);
	// }
	// }
	// }
	// return tl.get();
	// }

	/**
	 * yyyy-mm-dd HH:mm:ss格式的日期格式化对象
	 */
	public static final FastDateFormat timeFormat = FastDateFormat.getInstance(timeFormatStr);
	/**
	 * yyyy-MM-dd的日期格式化对象
	 */
	public static final FastDateFormat dateFormat = FastDateFormat.getInstance(dateFormatStr);
	/**
	 * yyyy-MM-dd HH:mm格式的日期格式化对象
	 */
	public static final FastDateFormat minuteFormat = FastDateFormat.getInstance(mimuteFormatStr);
	/**
	 * yyyy-MM-dd 00:00:00的日期格式化对象
	 */
	public static final FastDateFormat todayBegin = FastDateFormat.getInstance(todayBeginFormatStr);
	/**
	 * yyyy-MM-dd 23:59:59格式的日期格式化对象
	 */
	public static final FastDateFormat todayEnd = FastDateFormat.getInstance(todayEndFormatStr);
	/**
	 * yyyyMMddHHmmssSSS格式的日期格式化对象
	 */
	public static final FastDateFormat numMillFormat = FastDateFormat.getInstance(numMillFormatStr);
	static {
		// TimeZone zone = TimeZone.getTimeZone("GMT+08:00");
		// timeFormat.setTimeZone(zone);
		// dateFormat.setTimeZone(zone);
		// minuteFormat.setTimeZone(zone);
		// todayBegin.setTimeZone(zone);
		// todayEnd.setTimeZone(zone);
		// numMillFormat.setTimeZone(zone);
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		return "";
	}

	/**
	 * 获得当前日期字符串。格式为yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String currentDate() {
		return dateFormat.format(new Date());
	}

	/**
	 * 获得当前时间字符串。格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentTime() {
		return timeFormat.format(new Date());
	}

	/**
	 * 获得当前时间字符串，精确到分钟。格式为：yyyy-mm-dd HH:mm
	 * 
	 * @return
	 */
	public static String currentMinute() {
		return minuteFormat.format(new Date());
	}

	/**
	 * 获得精确到毫秒的当前时间字符串。格式为：yyyyMMddHHmmssSSS
	 * 
	 * @return
	 */
	public static String currentNumMill() {
		return numMillFormat.format(new Date());
	}

	/**
	 * 获得昨天的日期字符串。格式为yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String yesterDayDate() {
		return dateFormat.format(addDay(new Date(), -1));
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String dateToTimeString(Date date) {
		return timeFormat.format(date);
	}

	/**
	 * 获得19位长度的时间字符串，格式为：yyyy-MM-dd 00:00:00
	 * 
	 * @return
	 */
	public static String todayBegin() {
		return todayBegin.format(new Date());
	}

	/**
	 * 获得19位长度的时间字符串，格式为：yyyy-MM-dd 23:59:59
	 * 
	 * @return
	 */
	public static String todayEnd() {
		return todayEnd.format(new Date());
	}

	/**
	 * 获得时间字符串。格式为：yyyyMMdd
	 * 
	 * @return
	 */
	public static String Date2Num(String string, int length) {
		return numMillFormat.format(stringToDate(string)).substring(0, length - 1);
	}

	/**
	 * 把传入的时间增加指定的月份后返回。
	 * 
	 * @return
	 */
	public static String addMonth(String dateTime, int months, String formatStr) {
		if (formatStr == null)
			formatStr = dateFormatStr;
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		try {
			Date d = addMonth(sdf.parse(dateTime), months);
			return sdf.format(d);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 把传入的时间增加指定的小时后返回。
	 * 
	 * @return
	 */
	public static String addHour(String dateTime, int hours, String formatStr) {
		if (formatStr == null)
			formatStr = dateFormatStr;
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		try {
			Date d = addHour(sdf.parse(dateTime), hours);
			return sdf.format(d);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 把传入的时间增加指定的分钟后返回。
	 * 
	 * @return
	 */
	public static String addMinute(String dateTime, int min, String formatStr) {
		if (formatStr == null)
			formatStr = dateFormatStr;
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		try {
			Date d = addMinute(sdf.parse(dateTime), min);
			return sdf.format(d);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 日期增加小时
	 * 
	 * @param d
	 * @param months
	 * @return
	 */
	public static Date addHour(Date d, int hours) {
		Calendar cal = Calendar.getInstance(new Locale("zh-CN"));
		cal.setTime(d);
		cal.add(Calendar.HOUR, hours);
		return cal.getTime();
	}

	public static Date addMinute(Date d, int min) {
		Calendar cal = Calendar.getInstance(new Locale("zh-CN"));
		cal.setTime(d);
		cal.add(Calendar.MINUTE, min);
		return cal.getTime();
	}

	/**
	 * 日期增加月份
	 * 
	 * @param d
	 * @param months
	 * @return
	 */
	public static Date addMonth(Date d, int months) {
		Calendar cal = Calendar.getInstance(new Locale("zh-CN"));
		cal.setTime(d);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
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

	/**
	 * 获取的当前时间的Date
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance(new Locale("zh-CN"));
		return cal.getTime();
	}

	/**
	 * 把传入的时间字符串增加指定天数之后，返回原格式的字符串
	 * 
	 * @param dateTime
	 * @param days
	 * @param formatStr
	 * @return
	 */
	public static String addDay(String dateTime, int days, String formatStr) {
		if (formatStr == null)
			formatStr = dateFormatStr;
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		try {
			return sdf.format(addDay(sdf.parse(dateTime), days));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取这周的星期日期 add by HYL since 2009-08-07
	 * 
	 * @param i
	 *            星期几? 星期日用0表示
	 * @return
	 */
	public static String getThisWeekDay(int i) {
		Calendar cal = Calendar.getInstance(new Locale("zh-CN"));
		cal.setTime(new Date());
		cal.add(Calendar.DATE, i + 1 - cal.get(Calendar.DAY_OF_WEEK));
		return dateFormat.format(cal.getTime());
	}

	/**
	 * 获取上周的星期日期 add by HYL since 2009-08-07
	 * 
	 * @param i
	 *            星期几? 星期日用0表示
	 * @return
	 */
	public static String getLastWeekDay(int i) {
		Calendar cal = Calendar.getInstance(new Locale("zh-CN"));
		cal.setTime(new Date());
		cal.add(Calendar.DATE, i - 6 - cal.get(Calendar.DAY_OF_WEEK));
		return dateFormat.format(cal.getTime());
	}

	/**
	 * 将字符串型的时间转化成Date类型
	 * 
	 * @param timeStr
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date stringToDate(String timeStr) {
		TimeZone zone = TimeZone.getTimeZone("GMT+08:00");
		Calendar cal = Calendar.getInstance(zone);
		try {
			timeStr = timeStr.trim();
			int year = Integer.parseInt(timeStr.substring(0, 4));
			int month = Integer.parseInt(timeStr.substring(5, 7)) - 1;
			int date = Integer.parseInt(timeStr.substring(8, 10));
			int hour = Integer.parseInt(timeStr.substring(11, 13));
			int min = Integer.parseInt(timeStr.substring(14, 16));
			int sec = Integer.parseInt(timeStr.substring(17, 18));
			cal.set(year, month, date, hour, min, sec);
			Date d = cal.getTime();
			d.setHours(cal.get(Calendar.HOUR_OF_DAY));
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return cal.getTime();
		}
	}

	/**
	 * 计算 系统当前时间-输入时间 的时间间隔 单位：小时
	 * 
	 * @param date
	 * @return
	 */
	public static double disTime(String timeStr) {
		double d = (stringToDate(currentTime()).getTime()) - (stringToDate(timeStr).getTime());
		double dd = d / 3600000;
		return dd;
	}

	/** 把时间字符串从一种形式转换成另一种形式 */
	public static String convertTimeFormat(String timeStr, String srcFormatStr, String distFormatStr) {
		SimpleDateFormat srcFormat = new SimpleDateFormat(srcFormatStr);
		SimpleDateFormat distFormat = new SimpleDateFormat(distFormatStr);
		srcFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		distFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return convertTimeFormat(timeStr, srcFormatStr, distFormatStr);
	}

	/** 把时间字符串从一种格式转换成另一种格式 */
	public static String convertTimeFormat(String timeStr, SimpleDateFormat srcFormat, SimpleDateFormat distFormat) {
		if (timeStr == null || timeStr.length() == 0)
			return null;
		try {
			return distFormat.format(srcFormat.parse(timeStr));
		} catch (ParseException e) {
			return null;
		}
	}

	/** 把时间字符串从一种格式转换成另一种格式 */
	public static String timeFormat(String timeStr, String srcFormat, String distFormat) {
		if (timeStr == null || timeStr.length() == 0)
			return null;
		DateFormat srcFormat_01 = new SimpleDateFormat(srcFormat);
		DateFormat disFormat_01 = new SimpleDateFormat(distFormat);
		try {
			Date date = srcFormat_01.parse(timeStr);
			return disFormat_01.format(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 解析一个日期之间的所有月份
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList getMonthList(String beginDateStr, String endDateStr) {
		// 指定要解析的时间格式
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
		// 返回的月份列表
		String sRet = "";
		// 定义一些变量
		Date beginDate = null;
		Date endDate = null;
		GregorianCalendar beginGC = null;
		GregorianCalendar endGC = null;
		ArrayList list = new ArrayList();
		try {
			// 将字符串parse成日期
			beginDate = f.parse(beginDateStr);
			endDate = f.parse(endDateStr);
			// 设置日历
			beginGC = new GregorianCalendar();
			beginGC.setTime(beginDate);
			endGC = new GregorianCalendar();
			endGC.setTime(endDate);
			// 直到两个时间相同
			while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {
				sRet = beginGC.get(Calendar.YEAR) + "-" + (beginGC.get(Calendar.MONTH) + 1);
				list.add(sRet);
				// 以月为单位，增加时间
				beginGC.add(Calendar.MONTH, 1);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析一个日期段之间的所有日期
	 * 
	 * @param beginDateStr
	 *            开始日期
	 * @param endDateStr
	 *            结束日期
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList getDayList(String beginDateStr, String endDateStr) {
		// 指定要解析的时间格式
		SimpleDateFormat f = new SimpleDateFormat(dateFormatStr);
		// 定义一些变量
		Date beginDate = null;
		Date endDate = null;
		Calendar beginGC = null;
		Calendar endGC = null;
		ArrayList list = new ArrayList();
		try {
			// 将字符串parse成日期
			beginDate = f.parse(beginDateStr);
			endDate = f.parse(endDateStr);
			// 设置日历
			beginGC = Calendar.getInstance();
			beginGC.setTime(beginDate);
			endGC = Calendar.getInstance();
			endGC.setTime(endDate);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
			// 直到两个时间相同
			while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {
				list.add(sdf.format(beginGC.getTime()));
				// 以日为单位，增加时间
				beginGC.add(Calendar.DAY_OF_MONTH, 1);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getCurrYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 得到某一年周的总数
	 * 
	 * @param year
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LinkedHashMap getWeekList(int year) {
		LinkedHashMap map = new LinkedHashMap();
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
		int count = getWeekOfYear(c.getTime());

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
		String dayOfWeekStart = "";
		String dayOfWeekEnd = "";
		for (int i = 1; i <= count; i++) {
			dayOfWeekStart = sdf.format(getFirstDayOfWeek(year, i));
			dayOfWeekEnd = sdf.format(getLastDayOfWeek(year, i));
			map.put(new Integer(i), "第" + i + "周(从" + dayOfWeekStart + "至" + dayOfWeekEnd + ")");
		}
		return map;

	}

	/**
	 * 得到一年的总周数
	 * 
	 * @param year
	 * @return
	 */
	public static int getWeekCountInYear(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
		int count = getWeekOfYear(c.getTime());
		return count;
	}

	/**
	 * 取得当前日期是多少周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);

		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到某年某周的第一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getFirstDayOfWeek(cal.getTime());
	}

	/**
	 * 得到某年某周的最后一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getLastDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getLastDayOfWeek(cal.getTime());
	}

	/**
	 * 得到某年某月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getFirestDayOfMonth(int year, int month) {
		month = month - 1;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);

		int day = c.getActualMinimum(c.DAY_OF_MONTH);

		c.set(Calendar.DAY_OF_MONTH, day);
		return c.getTime();

	}

	/**
	 * 提到某年某月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getLastDayOfMonth(int year, int month) {
		month = month - 1;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		int day = c.getActualMaximum(c.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}

	/**
	 * 返回日期指示一个星期中的某天
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static int getDayOfWeek(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_WEEK, amount);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 取得当前日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得当前日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate(dateFormatStr);
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, dateFormatStr);
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, timeFormatStr);
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), timeFormatStr);
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return DateUtils.parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 字符串转LONG
	 * 
	 * @param time
	 * @return
	 */
	public static Long strTimeChangeLong(String time) {
		Calendar calBegin = new GregorianCalendar();
		try {
			calBegin.setTime(timeFormat.parse(time));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		long beginTime = calBegin.getTimeInMillis();
		return beginTime;
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	public static String subTime(String begin, String end) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calBegin = new GregorianCalendar();
		Calendar calEnd = new GregorianCalendar();
		try {
			calBegin.setTime(format.parse(begin));

		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			calEnd.setTime(format.parse(end));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		StringBuffer sb = new StringBuffer("");
		long temp = calEnd.getTimeInMillis() - calBegin.getTimeInMillis();
		if (86400000 < temp) {
			// sb.append((temp/86400000) + "天");
			temp = temp % 86400000;
		}
		if (3600000 < temp) {
			long hour = (temp / 3600000);
			sb.append((((hour + "").length() == 1 ? "0" + hour : hour)) + ":");
			temp = temp % 3600000;
		} else {
			sb.append("00:");
		}
		if (60000 < temp) {
			long min = (temp / 60000);
			if (min == 60) {
				min = min - 1;
			}
			sb.append((((min + "").length() == 1 ? "0" + min : min)) + ":");
			temp = temp % 60000;
		} else {
			sb.append("00:");
		}
		if (1000 < temp) {
			long second = temp / 1000;
			if (second == 60) {
				second = second - 1;
			}
			sb.append(((second + "").length() == 1 ? "0" + second : second));
		} else {
			sb.append("00");
		}
		return sb.toString();
	}

	public static boolean isValidDate(String str) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (Exception e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 字符串转换成日期yyyy-mm-dd HH:mm:ss
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {
		Date date = null;
		try {
			date = timeFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期比较大小，未来的时间大
	 * 
	 * @param strA
	 * @param strB
	 * @return
	 */
	public static boolean DateCompare(String strA, String strB) {
		if (strA.matches("\\d+-\\d+-\\d+ \\d+:\\d+")) {
			strA = strA + ":00";
		} else if (strA.matches("\\d+/\\d+/\\d+ \\d+:\\d+:\\d+")) {
			strA = strA.replaceAll("/", "-");
		} else if (strA.matches("\\d+/\\d+/\\d+ \\d+:\\d+")) {
			strA = strA.replaceAll("/", "-") + ":00";
		} else if (strA.matches("\\d+/\\d+/\\d+")) {
			strA = strA.replaceAll("/", "-") + " 00:00:00";
		} else if (strA.matches("\\d+-\\d+-\\d+")) {
			strA = strA + " 00:00:00";
		}
		if (strB.matches("\\d+-\\d+-\\d+ \\d+:\\d+")) {
			strB = strB + ":00";
		} else if (strB.matches("\\d+/\\d+\\/\\d+ \\d+:\\d+:\\d+")) {
			strB = strB.replaceAll("/", "-");
		} else if (strB.matches("\\d+/\\d+/\\d+ \\d+:\\d+")) {
			strB = strB.replaceAll("/", "-") + ":00";
		} else if (strB.matches("\\d+/\\d+/\\d+")) {
			strB = strB.replaceAll("/", "-") + " 00:00:00";
		} else if (strB.matches("\\d+-\\d+-\\d+")) {
			strB = strB + " 00:00:00";
		}
		return StrToDate(strA).getTime() > StrToDate(strB).getTime();
	}

	/**
	 * 给出最后一周的星期一的日期
	 * 
	 * @return
	 */
	public static List<String> printWeeks() {
		List<String> list = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		int month = calendar.get(Calendar.MONTH);
		int count = 0;
		while (calendar.get(Calendar.MONTH) == month) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				StringBuilder builder = new StringBuilder();
				builder.append("week:");
				builder.append(++count);
				builder.append(" (");
				builder.append(format.format(calendar.getTime()));
				list.add(format.format(calendar.getTime()));
				builder.append(" - ");
				calendar.add(Calendar.DATE, 6);
				builder.append(format.format(calendar.getTime()));
				builder.append(")");
			}
			calendar.add(Calendar.DATE, 1);
		}
		return list;
	}

	/**
	 * calendar类型转化为String类型
	 * 
	 * @param calendar
	 * @return
	 * 
	 */
	public static String convertCalendar(Calendar calendar) {
		return dateToTimeString(new Date(calendar.getTimeInMillis()));
	}

	/**
	 * 将字符类型转换为sql类日期类型
	 * 
	 * @param date
	 * @param pattern
	 * @return java.sql.Date
	 * @throws ParseException
	 */
	public static java.sql.Date parse(String date, String pattern) throws ParseException {
		return convert(new SimpleDateFormat(pattern).parse(date));
	}

	/**
	 * 将util类日期转换为sql类日期
	 * 
	 * @param date
	 * @return java.sql.Date
	 */
	public static java.sql.Date convert(Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * long转字符串
	 * 
	 * @param timelimit
	 * @return
	 */
	public static String longTimeChangeStr(Long timelimit) {
		Calendar current_calendar = new GregorianCalendar();
		current_calendar.setTimeInMillis(timelimit);
		return convertCalendar(current_calendar);
	}

	public static Calendar stringformatToCalendar(String date) {
		Calendar calendar;
		try {
			calendar = GregorianCalendar.getInstance();
			calendar.setTimeInMillis(parse(date, "yyyy-MM-dd HH:mm:ss").getTime());
		} catch (Exception e2) {
			e2.printStackTrace();
			calendar = null;
		}
		return calendar;
	}

	/***
	 * 从指定时间计算用户购买时间
	 * 
	 * @param timelimit
	 * @return
	 */
	public static String addTimeLimit(int timelimit, String date) {
		Calendar current_calendar = stringformatToCalendar(date);
		current_calendar.add(Calendar.DAY_OF_MONTH, timelimit);
		return convertCalendar(current_calendar);
	}

	/**
	 * 日期相减，返回长整型数值
	 * 
	 * @param begin
	 * @param end
	 * @return long
	 */
	public static long subDate(String begin, String end) {
		Calendar calBegin = new GregorianCalendar();
		Calendar calEnd = new GregorianCalendar();
		try {
			calBegin.setTime(timeFormat.parse(begin));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			calEnd.setTime(timeFormat.parse(end));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		long beginTime = calBegin.getTimeInMillis();
		long endTime = calEnd.getTimeInMillis();
		return beginTime - endTime;
	}

	/**
	 * 日期相减，返回长整型数值
	 * 
	 * @param begin
	 * @param end
	 * @return long
	 */
	public static long subDate(Date begin, Date end) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(begin);
		long beginTime = calendar.getTimeInMillis();
		calendar.setTime(end);
		long endTime = calendar.getTimeInMillis();
		return beginTime - endTime;
	}

	/**
	 * 获得当前日期时间
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}
}