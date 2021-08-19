package com.gen.vacation.global.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-07-31
 * Time: 오후 5:13
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
@Configuration
public class TransactionAspect {

    private final PlatformTransactionManager transactionManager;

    private static final int TX_METHOD_TIMEOUT = 300;
    private static final String EXPRESSION = "execution(* com.gen.vacation.*.*.service.*.*(..))";


    @Bean
    public TransactionInterceptor txAdvice() {

        TransactionInterceptor txAdvice = new TransactionInterceptor();

        Properties txAttributes = new Properties();

        List<RollbackRuleAttribute> rollbackRules = Collections.singletonList(new RollbackRuleAttribute(Exception.class));

        DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute(
                TransactionDefinition.PROPAGATION_REQUIRED);

        readOnlyAttribute.setReadOnly(true);

        readOnlyAttribute.setTimeout(TX_METHOD_TIMEOUT);

        RuleBasedTransactionAttribute writeAttribute = new RuleBasedTransactionAttribute(
                TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);

        writeAttribute.setTimeout(TX_METHOD_TIMEOUT);

        String readOnlyTransactionAttributesDefinition = readOnlyAttribute.toString();

        String writeTransactionAttributesDefinition = writeAttribute.toString();

        log.debug("Read Only Attributes :: {}", readOnlyTransactionAttributesDefinition);

        log.info("Write Attributes :: {}", writeTransactionAttributesDefinition);

        // read-only
        txAttributes.setProperty("sel*", readOnlyTransactionAttributesDefinition);

        // write rollback-rule

        txAttributes.setProperty("ins*", writeTransactionAttributesDefinition);
        txAttributes.setProperty("upd*", writeTransactionAttributesDefinition);
        txAttributes.setProperty("del*", writeTransactionAttributesDefinition);

        txAdvice.setTransactionAttributes(txAttributes);

        txAdvice.setTransactionManager(transactionManager);
        return txAdvice;

    }

    @Bean
    public Advisor txAdviceAdvisor() {

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();


        pointcut.setExpression(EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

}


