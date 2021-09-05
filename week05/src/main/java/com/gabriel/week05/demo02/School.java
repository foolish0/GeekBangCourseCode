package com.gabriel.week05.demo02;

import com.gabriel.week05.aop.ISchool;
import com.gabriel.week05.demo01.Student;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@Data
public class School implements ISchool {
    @Autowired
    Klass klass;

    @Resource(name = "student001")
    Student student;

    @Override
    public void interfaceAop() {
        System.out.println(
                "This class has " + klass.getStudents().size()
                        + " students and one of them is " + student
        );
    }
}
