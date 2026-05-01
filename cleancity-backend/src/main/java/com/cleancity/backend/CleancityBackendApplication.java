package com.cleancity.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CleancityBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CleancityBackendApplication.class, args);
        System.out.println("Application Started...");
    }
}