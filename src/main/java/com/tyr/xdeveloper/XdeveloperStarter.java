package com.tyr.xdeveloper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.tyr.xdeveloper.mapper")
public class XdeveloperStarter {

    public static void main(String[] args) {
        SpringApplication.run(XdeveloperStarter.class, args);
    }

}
                                                                                                                                 