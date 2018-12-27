package com.free.fasttools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.free.fasttools.dao.**")
@SpringBootApplication
public class FasttoolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FasttoolsApplication.class, args);
    }
}
