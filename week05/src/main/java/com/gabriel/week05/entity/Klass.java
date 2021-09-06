package com.gabriel.week05.entity;

import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;

@Data
public class Klass {
    private List<Student> students;

    public Klass(List<Student> students) {
        this.students = students;
    }


    public void classAop() {
        System.out.println(this.students);
    }
}
