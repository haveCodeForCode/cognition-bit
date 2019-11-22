package com.cognition.bit.common.until;

import java.io.UnsupportedEncodingException;

/**
 * 字符串工具
 * @author Worry
 * @version 2019/2/26
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    //分割字符常亮
    private static final char SEPARATOR = '_';
    //字符集常亮
    private static final String CHARSET_NAME = "UTF-8";


    /**
     * String转换为字节数组
     * @param str
     * @return
     */
    public static byte[] getBytes(String str){
        if (str != null){
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 数组装换为Stirng对象
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes){
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

}
