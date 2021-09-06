package com.gabriel.week05.entity;

import com.gabriel.week05.aop.ISchool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@Data
public class School implements ISchool {
    @Autowired
    Klass klass;

//    @Autowired
//    Student another;

    @Resource(name = "student01")
    Student student;

    @Override
    public void interfaceAop() {
        System.out.println(
                "This class has " + klass.getStudents().size()
                        + " students and one of them is " + student
        );
    }
}
