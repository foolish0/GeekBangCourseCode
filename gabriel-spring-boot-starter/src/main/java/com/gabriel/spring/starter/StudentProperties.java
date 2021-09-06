package com.gabriel.spring.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gabriel.student")
public class StudentProperties {
    private final Integer DEFAULT_ID = 99;
    private final String DEFAULT_NAME = "ROBOT";
    private Integer id;
    private String name;

    public StudentProperties() {
    }

    public StudentProperties(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public final StudentProperties defaultStudentProperties() {
        return new StudentProperties(DEFAULT_ID, DEFAULT_NAME);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
