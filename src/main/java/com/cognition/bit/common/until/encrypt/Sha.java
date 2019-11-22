package com.cognition.bit.common.until.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Worry
 * @version 2019/4/9
 */
public class Sha {

    public static byte[] encrypt(final String strText) {

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // Sha 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance("Sha-256");
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                return messageDigest.digest();
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        } else {
            return null;
        }

    }

}
