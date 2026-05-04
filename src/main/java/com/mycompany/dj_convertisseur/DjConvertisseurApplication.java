package com.mycompany.dj_convertisseur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan(basePackages = "com.mycompany.dj_convertisseur")
public class DjConvertisseurApplication {

    public static void main(String[] args) {
        SpringApplication.run(DjConvertisseurApplication.class, args);
    }
}