package com.cognition.bit.common.until;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具方法
 * @author Worry
 * @version 2019/3/14
 */
public class Tools {


    /**
     * 生成编码
     *
     * @param prefix 交易类型
     * @param num    第几个
     * @return 编码
     */
    public static String createNumCode(String prefix, int num) {
        //设置数字长度
        int numLength = 4;
        //声明当前时间年月日
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        //创建字符串拼接放法
        StringBuilder cString = new StringBuilder();
        cString.append(prefix);
        cString.append(day.format(new Date()));
        cString.append(String.format("%0" + numLength + "d", num));
        //字符串转换
        return String.valueOf(cString);
    }

    /**
     * 随机数
     * @param bitCount 返回的位数
     * @return 返回随机数
     */
    public static String getRandomNumber(int bitCount){
        long startValue =(long) Math.pow(10,bitCount-1);
        long endValue   =(long) Math.pow(10,bitCount);
        int rValue = (int) (Math.random()*(endValue-startValue)+startValue);
        return String.valueOf(rValue);
    }

    /**
     * 从字符串内获取地址信息
     *
     * @param address
     * @return
     */
    public static List<Map<String, String>> addressResolution(String address) {
        /*
         * java.util.regex是一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包。
         * 它包括两个类：Pattern和Matcher Pattern
         * 一个Pattern是一个正则表达式经编译后的表现模式。 Matcher
         * 一个Matcher对象是一个状态机器，它依据Pattern对象做为匹配模式对字符串展开匹配检查。
         * 首先一个Pattern实例订制了一个所用语法与PERL的类似的正则表达式经编译后的模式，然后一个Matcher实例在这个给定的Pattern实例的模式控制下进行字符串的匹配工作。
         */
        String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher matcher = Pattern.compile(regex).matcher(address);
        String province = null, city = null, county = null, town = null, village = null;

        List<Map<String, String>> table = new ArrayList<Map<String, String>>();

        while (matcher.find()) {
            Map<String, String> row = new LinkedHashMap<String, String>();
            province = matcher.group("province");
            row.put("province", province == null ? "" : province.trim());
            city = matcher.group("city");
            row.put("city", city == null ? "" : city.trim());
            county = matcher.group("county");
            row.put("county", county == null ? "" : county.trim());
            town = matcher.group("town");
            row.put("town", town == null ? "" : town.trim());
            village = matcher.group("village");
            row.put("village", village == null ? "" : village.trim());
            table.add(row);
        }
        return table;
    }

}
