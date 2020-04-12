package com.tao.hai.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AesUtils {
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * AES加密方式
     */
    public static String encrypt(String content, String key) {
        try {
            byte[] raw = key.getBytes();  //获得密码的字节数组
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //根据密码生成AES密钥
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  //根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.ENCRYPT_MODE, skey); //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            byte[] byte_content = content.getBytes(StandardCharsets.UTF_8); //获取加密内容的字节数组(设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] encode_content = cipher.doFinal(byte_content); //密码器加密数据
            return Base64.encodeBase64String(encode_content); //将加密后的数据转换为字符串返回
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密方式
     */
    public static String decrypt(String encryptStr, String decryptKey) {
        try {
            byte[] raw = decryptKey.getBytes();  //获得密码的字节数组
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //根据密码生成AES密钥
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  //根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.DECRYPT_MODE, skey); //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            byte[] encode_content = Base64.decodeBase64(encryptStr); //把密文字符串转回密文字节数组
            byte[] byte_content = cipher.doFinal(encode_content); //密码器解密数据
            return new String(byte_content, StandardCharsets.UTF_8); //将解密后的数据转换为字符串返回
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
