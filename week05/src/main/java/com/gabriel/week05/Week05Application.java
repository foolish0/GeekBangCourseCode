package com.gabriel.week05;

import com.gabriel.spring.starter.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lizhenjiang
 */
@SpringBootApplication
public class Week05Application {
    @Autowired
    static private StudentService service;

    public static void main(String[] args) {
        service.student();
        //SpringApplication.run(Week05Application.class, args);
    }

}
