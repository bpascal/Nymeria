package com.codido.nymeria.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间数据处理相关工具类
 */
public class TimeProcessUtils {

	private static int parseInt;
	private static int parseInt2;
	private static int parseInt3;
	private static int parseInt4;
	private static int parseInt5;
	private static int parseInt6;
	private static int parseInt7;
	private static int parseInt8;

	/**
	 * 根据格式获取当前时间
	 *
	 * @param format
	 * @return
	 */
	public static String getFormatTime(String format) {
		String time = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			time = dateFormat.format(new Date());
		} catch (Exception e) {
			time = "时间获取出错";
		}
		return time;
	}

	/**
	 * 将时间戳转换为正常时间格式
	 *
	 * @param currentTimeMillis
	 * @return
	 */
	public static String getTimeFormat(long currentTimeMillis) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(currentTimeMillis);
		return dateFormat.format(date);
	}

	/**
	 * 时间戳转为日期字符
	 * @param seconds
	 * @param format
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * 日期字符转为时间戳
	 * @param date_str
	 * @param format
	 * @return
	 */
	public static String date2TimeStamp(String date_str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
