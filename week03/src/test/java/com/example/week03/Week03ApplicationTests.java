package com.example.week03;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Week03ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void sub() {
        String backend = "123.3.3.3/";
        String result = backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;

        assert backend.equals(result + "/");
    }

}
