package com.gabriel.week05.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public class Aop01 {
    /**
     * 前置通知
     */
    public void beforeMyAop() {
        System.out.println("#### before classAop #### ====>2");
    }

    /**
     * 后置通知
     */
    public void afterMyAop() {
        System.out.println("#### after classAop #### ====>4");
    }

    /**
     * 环绕通知
     *
     * @param joinPoint 切点
     * @throws Throwable joinPoint.proceed()方法执行抛出异常
     */
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("#### before around classAop #### ====>1");

        joinPoint.proceed();

        System.out.println("#### after around classAop #### ====>3");
    }
}
