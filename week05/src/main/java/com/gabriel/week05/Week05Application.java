package com.gabriel.week05;

import com.gabriel.spring.starter.StudentProperties;
import com.gabriel.spring.starter.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author lizhenjiang
 */
@SpringBootApplication
public class Week05Application implements CommandLineRunner {
    @Resource
    private StudentService service;
    @Resource
    private StudentProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(Week05Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(service.student());
        System.out.println(properties.getId() + properties.getName());
    }
}
