package com.supercoding.commerce03.web.advice;

import com.supercoding.commerce03.config.DataSourceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class DataSourceAspect {

    //@Pointcut 어노테이션은 Spring AOP에서 특정 조인포인트를 정의
    //@Repository 어노테이션이 붙은 클래스 내부의 모든 메서드
    @Pointcut("@within(org.springframework.stereotype.Service)") // Service 어노테이션이 붙은 클래스의 메서드
    public void inService() {}

    //aka AspectJ 표현식. *(임의의 리턴타입) *.*(임의의 클래스 내의 임의의 메서드) (...)(임의의 매개변수)
    //즉, execution(* *.*(..)) 는 모든 클래스 내의 모든 매서드의 실행시점.
    @After("inService() && execution(* *.*(..))") // Repository 메서드 실행 전
    //JoinPoint : 프록시가 호출되는 지점을 나타내는 객체로, AOP에서 Advice(어드바이스)를 적용하는 지점을 식별하는 데 사용
    //joinPoint.getSignature()은 현재 JoinPoint의 시그니처를 반환. 시그니처는 메서드의 이름과 매개변수 정보 등을 포함한 메서드의 고유한 식별자
    public void afterServiceMethodExecution(JoinPoint joinPoint) {
        String dataSourceKey = determineDataSource();
        log.info("\u001B[32mDataSource: {}", dataSourceKey);
    }

    // Signature 인터페이스 자체에는 메서드의 이름이나 매개변수 등의 정보를 추출할 수 있는 메서드가 없다.
    //실제로 메서드의 시그니처 정보를 추출하려면 Signature 인터페이스를 구체적인 클래스로 캐스팅해야 함.
    // MethodSignature 클래스에는 메서드 이름, 리턴 타입, 매개변수 등의 정보를 얻을 수 있는 메서드가 구현.
    private String determineDataSource() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        String dataSourceKey = isReadOnly ? "slave" : "master";

        return dataSourceKey != null ? dataSourceKey.toString() : "Unknown";
        }
    }

