package com.blog.api.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Md5Util {

    public static String md5(String pwd) {
        String MD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pwd.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }

}
