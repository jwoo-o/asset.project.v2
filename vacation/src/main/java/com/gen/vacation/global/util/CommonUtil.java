package com.gen.vacation.global.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-04-22
 * Time: 오후 2:42
 */
@Slf4j
public class CommonUtil {

    private CommonUtil() { throw new IllegalStateException("Utility class"); }

    public static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String checkDefaultNull(Object obj, String defaultVal) {
        String str = "";
        if (obj == null) {
            return defaultVal;
        } else {
            try {
                str = obj.toString();
            } catch (Exception e) {
                return defaultVal;
            }
            return str;
        }
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        log.debug("client_ip : {}",ip);

        return ip;
    }

    public static LocalDateTime stringToLocalDateTime(String date) {

        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
    public static LocalDate stringToLocalDate(String date) {

        return  LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }
    public static String korLocalDateTime(LocalDateTime date) {

        return date.format(DateTimeFormatter.ofPattern("yyyy년  MM월  dd일"));
    }
    public static String korLocalDate(LocalDate date) {

        return date.format(DateTimeFormatter.ofPattern("yyyy년  MM월  dd일"));
    }

    public static List<String> findBetweenCurAndHire(int hire) {

        List<String> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String endYear = dateFormat.format(cal.getTime());

        cal.set(hire, Calendar.FEBRUARY,1);
        String startYear = dateFormat.format(cal.getTime());

        list.add(endYear);

        while(!startYear.equals(endYear)){
            list.add(startYear);
            cal.add(Calendar.YEAR,+1);
            startYear = dateFormat.format(cal.getTime());
        }

        return list;
    }



    public static String formatD(double number) {

        DecimalFormat df=new DecimalFormat("#.##");
        return df.format(number);

    }



}
