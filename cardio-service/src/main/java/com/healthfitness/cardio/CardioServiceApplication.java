package com.healthfitness.cardio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CardioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardioServiceApplication.class, args);
    }
}
