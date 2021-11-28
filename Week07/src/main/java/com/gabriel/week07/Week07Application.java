package com.gabriel.week07;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Week07Application {

    public static void main(String[] args) {
        SpringApplication.run(Week07Application.class, args);

        List<Integer> list = Arrays.asList(1, 4, 21, 3, 21, 4, 2, 6, 34, 67);
        String join = Joiner.on(",").join(list);
        System.out.println(join);

        String str = "12湖大煞风景,jife,jdf,df,,,,,,e,23,,,12";
        List<String> strings = Splitter.on(",").splitToList(str);
        System.out.println(strings);

        System.out.println(0%2);
        System.out.println(1%2);

    }

}
