package com.gen.vacation.global.util;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-04-13
 * Time: 오후 7:33
 */
public class IpUtil {

    private IpUtil() { throw new IllegalStateException("Utility class"); }

    public static Long ipToInt(String addr) {
        String[] addrArray = addr.split("\\.");
        long ip = 0;
        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            ip += (Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power));
        }
        return ip;
    }
}
