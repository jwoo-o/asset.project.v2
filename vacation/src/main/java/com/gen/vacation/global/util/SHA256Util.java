package com.gen.vacation.global.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-02-02
 * Time: 오후 4:40
 */
public class SHA256Util {

    private SHA256Util() { throw new IllegalStateException("Utility class"); }


    /**
     * SHA-256 암호화 함
     * @param source 원본
     * @param salt(String) SALT 값
     * @return
     */
    public static String getEncrypt(String source, String salt) {
        return getEncrypt(source, salt.getBytes());
    }

    /**
     * SHA-256 암호화 함
     * @param source 원본
     * @param salt(byte[]) SALT 값
     * @return
     */
    public static String getEncrypt(String source, byte[] salt) {

        String result = "";

        byte[] a = source.getBytes();
        byte[] bytes = new byte[a.length + salt.length];

        System.arraycopy(a, 0, bytes, 0, a.length);
        System.arraycopy(salt, 0, bytes, a.length, salt.length);

        try {
            // 암호화 방식 지정 메소드
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);

            byte[] byteData = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
            }

            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }


}
