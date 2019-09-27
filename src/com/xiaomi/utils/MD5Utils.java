package com.xiaomi.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Utils {
    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("utf-8"));
            byte[] digest = messageDigest.digest();
            //1表示正数
            BigInteger bigInteger = new BigInteger(1, digest);
            return bigInteger.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
