package com.gabriel.week05.aopdemo;

import com.gabriel.week05.aop.ISchool;
import com.gabriel.week05.entity.Klass;
import com.gabriel.week05.entity.School;
import com.gabriel.week05.entity.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopDemo {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beanConfig.xml");
        Student student01 = (Student) context.getBean("student01");
        student01.print();

        Student student02 = (Student) context.getBean("student02");
        student02.print();

        System.out.println("====================");

        Klass klass = context.getBean(Klass.class);
        System.out.println(klass.getClass());
        klass.classAop();

        System.out.println("====================");

        ISchool school = context.getBean(ISchool.class);
        System.out.println(school.getClass());
        school.interfaceAop();
    }
}
