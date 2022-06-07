package com.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UsianLeadnewsUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsianLeadnewsUserApplication.class, args);
    }

}
