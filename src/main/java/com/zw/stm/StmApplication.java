package com.zw.stm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zw.stm.mapper")
public class StmApplication {

    public static void main(String[] args) {
        SpringApplication.run(StmApplication.class, args);
    }

}
