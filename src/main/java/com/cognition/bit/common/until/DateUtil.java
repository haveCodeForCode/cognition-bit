package com.cognition.bit.common.until;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 时间工具
 * @author Worry
 * @version 2019/3/14
 */
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {

    /**
     * 时间串格式
     */
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 格式化日期
     *
     * @param date 时间
     * @param type 类型 （列如："yyyy-MM-dd"）
     * @return
     */
    private static Date fomatDate(String date, String type) {
        if (StringUtils.isNotBlank(date)) {
            return null;
        }
        DateFormat fmt = new SimpleDateFormat(type);
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     * pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    private static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
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
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
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
     * 得到当前的小时
     *
     * @return 时间字符串
     */
    public static String getHours() {
        return formatDate(new Date(), "HH");
    }

    /**
     * 得到当前年份+月份字符串 格式（yyyy-MM）
     */
    public static String getYearMonth() {
        return formatDate(new Date(), "yyyy-MM");
    }


    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 时间戳转yyyy
     *
     * @param millis 秒数
     * @return 年
     */
    public static String getFormatYear(Long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return LocalDate.from(cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).format(DateTimeFormatter.ofPattern("yyyy"));
    }

    /**
     * 获取过去的天数
     *
     * @param date 时间类型
     * @return 天数
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date 时间类型
     * @return 小时数
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date 时间类型
     * @return 分钟数
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis 秒（long）
     * @return 时：分：秒
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
     * @param before 过去天数
     * @param after  之后天数
     * @return 返回天数
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 返回两个时间相减的分钟数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 分钟数
     */
    public static long dateReduction(Date start, Date end) {
        long l = end.getTime() - start.getTime();
        return l / 1000 / 60;
    }

    /**
     * @param s 字符串之前时间
     * @param e 字符串之后时间
     * @return boolean
     * @Title: compareDate
     * @Description: TODO(日期比较 ， 如果s > = e 返回true 否则返回false)
     * @author luguosui
     */
    public static boolean compareDate(String s, String e) {
        if (fomatDate(s, "yyyy-MM-dd") == null || fomatDate(e, "yyyy-MM-dd") == null) {
            return false;
        }
        return Objects.requireNonNull(fomatDate(s, "yyyy-MM-dd")).getTime() >= Objects.requireNonNull(fomatDate(e, "yyyy-MM-dd")).getTime();
    }

    /**
     * @param s 之前时间的时间类型
     * @param e 之后时间的时间类型
     * @return boolean
     * @Title: compareDate
     * @Description: TODO(日期比较 ， 如果s > = e 返回true 否则返回false)
     * @author luguosui
     */
    public static boolean compareDate(Date s, Date e) {
        if (s == null || e == null) {
            return false;
        }
        return s.getTime() >= e.getTime();
    }

    /**
     * 获取日期中的天
     *
     * @param date
     * @return
     */
    public static String getDay(Date date) {
        if (date == null) {
            return "0";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(day);
    }

    /**
     * 获取两个日期之间的所有日期
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<String> getTimeString(Date beginDate, Date endDate) {
        SimpleDateFormat sdfCurrentDayTime = new SimpleDateFormat(
                "yyyy-MM-dd 00:00:00");
        List list = new ArrayList();
        //把开始时间加入集合
        list.add(sdfCurrentDayTime.format(beginDate));
        Date date = new Date();
        if (compareDate(endDate, date)) {
            endDate = date;
        }
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        if (cal.get(Calendar.DAY_OF_YEAR) == calEnd.get(Calendar.DAY_OF_YEAR)) {
            return list;
        }
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                list.add(sdfCurrentDayTime.format(cal.getTime()));
            } else {
                break;
            }
        }
        //把结束时间加入集合
        list.add(sdfCurrentDayTime.format(endDate));
        return list;

    }

    /**
     * 获取当前时间今天剩余多少秒
     *
     * @return
     */
    public static int getOverDateSeconds() {
        Date date = new Date();
        LocalDateTime midnight = LocalDateTime.ofInstant(date.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(date.toInstant(),
                ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }


    /**
     * 获取今天0点的日期 返回date
     *
     * @return 返回date
     */
    public static Date getCurrentDayTimeDate() {
        SimpleDateFormat sdfCurrentDayTime = new SimpleDateFormat(
                "yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdfTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdfTime.parse(sdfCurrentDayTime.format(new Date()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }
}
