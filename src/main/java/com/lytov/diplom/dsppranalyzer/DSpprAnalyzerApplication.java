package com.lytov.diplom.dsppranalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DSpprAnalyzerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DSpprAnalyzerApplication.class, args);
    }
}
