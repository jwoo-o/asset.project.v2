package com.gen.vacation.global.aop;

import com.gen.vacation.global.util.CommonUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-08-03
 * Time: 오전 9:15
 */
@Slf4j
@Component
@Aspect
public class RequestLoggingAspect {


    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }


    @Pointcut("execution(* com.gen.vacation.server.*.web.*.*(..))")
    public void onRequest() {
    }

    @Around("com.gen.vacation.global.aop.RequestLoggingAspect.onRequest()") // 4
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = // 5
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object paramMap = null;
        String params = "";
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            params += " [";
            Object[] objects = pjp.getArgs();
            for (Object object : objects) {

                if (!object.getClass().toString().contains("java")) {
                    params += object.toString();
                }
            }
            params += "]";
        } else {
            paramMap = request.getParameterMap();
            if (!((Map) paramMap).isEmpty()) {
                params = " [" + paramMapToString((Map<String, String[]>) paramMap) + "]";
            }
        }

        long start = System.currentTimeMillis();
        try {
            return pjp.proceed(pjp.getArgs()); // 6
        } finally {
            long end = System.currentTimeMillis();
            log.info("Request: {} {}{} < {} ({}ms)", request.getMethod(), request.getRequestURI(),
                    params, CommonUtil.getClientIpAddr(request), end - start);
        }
    }
}

