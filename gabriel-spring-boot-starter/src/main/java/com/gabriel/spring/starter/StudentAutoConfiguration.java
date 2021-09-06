package com.gabriel.spring.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lizhenjiang
 */
@Configuration
@EnableConfigurationProperties(StudentProperties.class)
@ConditionalOnClass(StudentService.class)
@ConditionalOnProperty(prefix = "gabriel.student", name = "enabled", havingValue = "true")
public class StudentAutoConfiguration {

    public StudentService studentService() {
        return new StudentService();
    }

}
