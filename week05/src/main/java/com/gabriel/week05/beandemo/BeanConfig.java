package com.gabriel.week05.beandemo;

import com.gabriel.week05.entity.Klass;
import com.gabriel.week05.entity.School;
import com.gabriel.week05.entity.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class BeanConfig {
    @Bean
    public Student student() {
        return new Student();
    }

    @Bean
    public Klass klass(Student student) {
        return new Klass(Collections.singletonList(student));
    }

}
