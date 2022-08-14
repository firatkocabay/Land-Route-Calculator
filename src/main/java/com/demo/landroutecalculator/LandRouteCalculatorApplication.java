package com.demo.landroutecalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LandRouteCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LandRouteCalculatorApplication.class, args);
    }

}
