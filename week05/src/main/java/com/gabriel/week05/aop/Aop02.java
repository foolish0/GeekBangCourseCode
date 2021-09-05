package com.gabriel.week05.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aop02 {
    @Pointcut(value = "execution(* com.gabriel.week05.aop.*.interface*(..))")
    public void point() {

    }

    @Before(value = "point()")
    public void before() {
        System.out.println("#### before interfaceAop #### ====>2");
    }

    @After(value = "point()")
    public void after() {
        System.out.println("#### after interfaceAop #### ====>4");
    }

    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("#### before around interfaceAop #### ====>1");

        joinPoint.proceed();

        System.out.println("#### after around interfaceAop #### ====>3");
    }
}
