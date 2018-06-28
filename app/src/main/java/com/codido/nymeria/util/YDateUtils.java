package com.codido.nymeria.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class YDateUtils {

    public static int getDayOfWeek() {
        Calendar calendar1 = Calendar.getInstance();
        return calendar1.get(Calendar.DAY_OF_WEEK);
    }

    public static int[] getTimeInterval(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int year = 0;
        int day = 0;
        while (true) {
            long seconds = calendar1.getTime().getTime() - calendar2.getTime().getTime();
            if (seconds == 0) {
                System.out.println("-时间间隔相差" + year + "年" + day + "天");
                return new int[]{year, day};
            } else if (seconds > 0) {
                year--;
                break;
            }
            calendar1.add(Calendar.YEAR, 1);
            year++;
        }

        // 多添加了一年，先减去这一年
        calendar1.add(Calendar.YEAR, -1);

        day = (int) ((calendar2.getTime().getTime() - calendar1.getTime().getTime() - 1) / ONE_DAY + 1);

        System.out.println("时间间隔相差" + year + "年" + day + "天");

        return new int[]{year, day};
    }

    public static final long ONE_DAY = 86400000L;

    public static Date[] getWeekStartAndEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 2);

        Date[] dates = new Date[2];
        dates[0] = calendar.getTime();

        calendar.set(Calendar.DAY_OF_WEEK, 6);
        dates[1] = calendar.getTime();


        return dates;
    }

    public static String[] getThisMonthStartAndEndDay(Date month) {
        String[] dates = new String[2];

        Calendar calendar = Calendar.getInstance();
        if (month == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(month);
        }
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dates[0] = format(calendar.getTime(), "yyyyMMdd");

        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        dates[1] = format(calendar.getTime(), "yyyyMMdd");

        return dates;
    }

    private static HashMap<String, SimpleDateFormat> formats;

    static {
        formats = new HashMap<String, SimpleDateFormat>();
    }

    public static Date parser(String sDate, String pattern) {
        if (sDate == null) {
            return null;
        }
        try {
            SimpleDateFormat format = formats.get(pattern);
            if (format == null) {
                format = new SimpleDateFormat(pattern);
                formats.put(pattern, format);
            }
            return format.parse(sDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat format = formats.get(pattern);
            if (format == null) {
                format = new SimpleDateFormat(pattern);
                formats.put(pattern, format);
            }
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * // date类型转换为String类型
     * // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * // data Date类型的时间
     *
     * @param data
     * @param formatType
     * @return
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /**
     * // long类型转换为String类型
     * // currentTime要转换的long类型的时间
     * // formatType要转换的string类型的时间格式
     *
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static String longToString(long currentTime, String formatType) throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * // string类型转换为date类型
     * // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
     * // HH时mm分ss秒，
     * // strTime的时间格式必须要与formatType的时间格式相同
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime, String formatType) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * // long转换为Date类型
     * // currentTime要转换的long类型的时间
     * // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     *
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date longToDate(long currentTime, String formatType) throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    /**
     * // string类型转换为long类型,strTime要转换的String类型的时间
     * // formatType时间格式
     * // strTime的时间格式和formatType的时间格式必须相同
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType) throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * date类型转换为long类型
     *
     * @param date
     * @return
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 把间隔的秒数转为日常阅读类型
     *
     * @param millis
     * @return
     */
    public static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(days);
        sb.append(" 天 ");
        sb.append(hours);
        sb.append(" 小时 ");
        sb.append(minutes);
        sb.append(" 分 ");
        sb.append(seconds);
        sb.append(" 秒");

        return (sb.toString());
    }

    /**
     * 获取当前的时间日期
     *
     * @return
     */
    public static String getCurrentTimeWithAM() {
        Long dateLong = System.currentTimeMillis();
        String dateStr = null;
        try {
            dateStr = longToString(dateLong, "yyyyMMdd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeAm = getCurrentTimeAm();
        Global.debug("bpascal timeStr is:" + dateStr + timeAm);
        return dateStr + timeAm;
    }

    /**
     * 获取上午下午
     */
    private static String getCurrentTimeAm() {
        String retStr = null;
        GregorianCalendar ca = new GregorianCalendar();
        int amTime = ca.get(GregorianCalendar.AM_PM);
        if (amTime == 0) {
            retStr = "AM";
        } else {
            retStr = "PM";
        }
        return retStr;
    }
}
