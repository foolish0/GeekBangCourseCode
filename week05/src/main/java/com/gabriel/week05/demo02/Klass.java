package com.gabriel.week05.demo02;

import com.gabriel.week05.demo01.Student;
import lombok.Data;

import java.util.List;

@Data
public class Klass {
    private List<Student> students;

    public void classAop() {
        System.out.println(this.students);
    }
}
