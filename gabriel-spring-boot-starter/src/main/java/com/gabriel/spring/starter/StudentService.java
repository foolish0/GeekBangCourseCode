package com.gabriel.spring.starter;

public class StudentService {
    private static StudentProperties properties;

    public StudentService() {
        this(properties.defaultStudentProperties());
    }

    public StudentService(StudentProperties properties) {
        StudentService.properties = properties;
    }

    public String student() {
        Integer id = properties.getId();
        String name = properties.getName();
        return  "[学生ID: " + id
                + ", 学生姓名: " + name
                + "]";
    }
}
