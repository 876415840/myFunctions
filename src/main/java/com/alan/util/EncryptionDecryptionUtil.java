package com.alan.util;

import java.security.MessageDigest;

/**
 * @Description: 加密、解密
 * @Author MengQingHao
 * @Date 2020/4/30 11:12 上午
 * @Version 1.3.0
 */
public class EncryptionDecryptionUtil {

    public static final String SHA256 = "SHA-256";
    public static final String ENCODER = "UTF-8";

    public static String encryptSha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA256);
            byte[] hash = digest.digest(input.getBytes(ENCODER));
            // 十六进制散列
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
