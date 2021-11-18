package com.gen.vacation.global.aop;

import com.gen.vacation.global.common.dto.SearchRequestDto;
import com.gen.vacation.server.organization.repository.OrganizationRepositorySupport;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-24
 * Time: 오후 1:38
 */
@Slf4j
@Component
@Aspect
public class BeforeOrgCodesAspect {

    @Autowired
    private OrganizationRepositorySupport organizationRepositorySupport;

    @Pointcut("execution(* com.gen.vacation.server.*.service.*.*ByOrgCodes(..))")
    public void onRequest() {}

    @Around("com.gen.vacation.global.aop.BeforeOrgCodesAspect.onRequest()") // 4
    public Object doSearch(ProceedingJoinPoint pjp) throws Throwable {

        SearchRequestDto dto = (SearchRequestDto) pjp.getArgs()[0];
        if (dto.getOrder() > 1) {
            List<String> orgCodes = organizationRepositorySupport.findOrgCodeAllByOrgCode(dto.getOrgCode(), dto.getOrder());
            dto.setCodes(orgCodes);
        }

        return pjp.proceed(pjp.getArgs());
    }
}
