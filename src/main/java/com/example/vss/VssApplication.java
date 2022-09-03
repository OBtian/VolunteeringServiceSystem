package com.example.vss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.vss")
//@ComponentScan(basePackages = {"com.example.vss.config"})
public class VssApplication {

    public static void main(String[] args) {
        SpringApplication.run(VssApplication.class, args);
    }

}
