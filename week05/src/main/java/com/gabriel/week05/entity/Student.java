package com.gabriel.week05.entity;

import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
public class Student {
    private Integer id;
    private String name;

    public void print() {
        System.out.println(this);
    }


}
