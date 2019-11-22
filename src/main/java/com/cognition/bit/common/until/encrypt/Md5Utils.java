package com.cognition.bit.common.until.encrypt;


import com.cognition.bit.common.config.Constant;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * Md5工具类
 *
 * @author 王睿
 * @version 2018/12/25
 */
public class Md5Utils {

    private static final String CHARSET = "UTF-8";

    private static final String RSA_ALGORITHM = "RSA";

    private static final String SALT = "Wr960212!";

    private static final String ALGORITH_NAME = "md5";

    private static final int HASH_ITERATIONS = 2;


    /**
     * md5加密密码
     * @param pswd
     * @return
     */
    public static String encrypt(String pswd) {
        String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(SALT), HASH_ITERATIONS).toHex();
        return newPassword;
    }

    /**
     * md5加密组合密码
     * @param username
     * @param pswd
     * @return
     */
    public static String encrypt(String username, String pswd) {
        String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(username + SALT),
                HASH_ITERATIONS).toHex();
        return newPassword;
    }

    /**
     * MD5方法
     *
     * @param key 密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String key) throws Exception {
        //加密后的字符串
        Map<String, String> keyMap = Md5Utils.createKeys(512);
        String privateKey = keyMap.get("privateKey");
        String encodedData = Md5Utils.publicEncrypt(key, Md5Utils.getPublicKey(privateKey));
        return encodedData;
    }

    /**
     * MD5验证方法
     *
     * @param encodedData 密钥
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String key, String encodedData) throws Exception {
        //根据传入的密钥进行验证
        Map<String, String> keyMap = Md5Utils.createKeys(512);
        String privateKey = keyMap.get("privateKey");
        String decodedData = Md5Utils.privateDecrypt(encodedData, Md5Utils.getPrivateKey(privateKey));
        if (decodedData.equals(key)) {
            return true;
        }
        return false;
    }


    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //密匙类型  加密解密  串长度  密匙长度
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }


    /**
     * 公钥解密
     *
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            System.out.println("公钥解除封印");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            //密匙类型  加密解密  串长度  密匙长度
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }


    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            //获取
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //密匙类型  加密解密  串长度  密匙长度
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            System.out.println("封印");
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            //密匙类型  加密解密  串长度  密匙长度
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 对于串长度比较长的算法
     *
     * @param cipher
     * @param opmode  加密还是解密
     * @param datas
     * @param keySize
     * @return
     */

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        //个钥匙的最多位数   完成的长度     次数
        int maxBlock = 0, complete = 0, times = 0;
        //执行后
        byte[] afterimplement;
        //判断加密还是解密
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        //输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            while (datas.length > complete) {
                if (datas.length - complete > maxBlock) {
                    afterimplement = cipher.doFinal(datas, complete, maxBlock);
                } else {
                    afterimplement = cipher.doFinal(datas, complete, datas.length - complete);
                }
                out.write(afterimplement, 0, afterimplement.length);
                times++;
                complete = times * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        return out.toByteArray();
    }


    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws NoSuchAlgorithmException
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = null;
        try {
            key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        //
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 根据长度生成RAS密匙
     * 返回公/私密匙对象
     *
     * @param keySize
     * @return
     */
    public static Map<String, String> createKeys(int keySize) {
        Map<String, String> keyPairMap = new HashMap<String, String>();
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对（生成公私两钥）
        KeyPair keyPair = kpg.generateKeyPair();

        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        keyPairMap.put("publicKey", publicKeyStr);

        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 加解密方法实现
     *
     * @return ENCRY返回加密后的字符串
     * DECRY返回解密后的字符串
     */
    public static Map<String, Object> encryAndDecryption(String key, String keyState) {
        String state = Constant.FAIL;
        Map<String, String> keyMap = Md5Utils.createKeys(512);

        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");

        Map<String, Object> codeData = new HashMap<>();
        try {
            if (Constant.ENCRY.equals(keyState)) {
                String data = Md5Utils.publicEncrypt(key, Md5Utils.getPublicKey(publicKey));
                state = Constant.SUCCESS;
                codeData.put("data", data);
                codeData.put("state", state);
            }
            if (Constant.DECRY.equals(keyState)) {
                String data = Md5Utils.privateDecrypt(key, Md5Utils.getPrivateKey(privateKey));
                state = Constant.SUCCESS;
                codeData.put("data", data);
                codeData.put("state", state);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            codeData.put("state", state);
            codeData.put("data", e);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            codeData.put("state", state);
            codeData.put("data", e);
        }
        return codeData;
    }


    /**
     * 加/解密案例
     *
     * @param args
     */
    public static void main(String[] args) {


      String encodeKey = Md5Utils.encrypt("566956334545633280", "123456");
      System.out.println(encodeKey);
//        Map<String, String> keys = new HashMap<>();
//        Map<String, String> keyMap = Md5Utils.createKeys(64);
//        String publicKey = keyMap.get("publicKey");
//        String privateKey = keyMap.get("privateKey");
//        String str = "123456H#$kmrC$MLxI9R6%";
//        String encodedData = "";
//        String decodedData = "";
//        try {
//            encodedData = Md5Utils.publicEncrypt(str, Md5Utils.getPublicKey(publicKey));
//            decodedData = Md5Utils.privateDecrypt(encodedData, Md5Utils.getPrivateKey(privateKey));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        System.out.println(encodedData);
//        System.err.println(decodedData);
    }
}


