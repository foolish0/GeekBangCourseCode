package com.gabriel.spring.starter;

public class StudentService {
    private StudentProperties properties;

    public StudentService() {
        //this(properties.defaultStudentProperties());
    }

    public StudentService(StudentProperties properties) {
        this.properties = properties;
    }

    public String student() {
        if (null == this.properties) {
            properties = StudentProperties.defaultStudentProperties();
        }
        Integer id = properties.getId();
        String name = properties.getName();
        return  "[学生ID: " + id
                + ", 学生姓名: " + name
                + "]";
    }

    public StudentProperties getProperties() {
        return properties;
    }

    public void setProperties(StudentProperties properties) {
        this.properties = properties;
    }
}
