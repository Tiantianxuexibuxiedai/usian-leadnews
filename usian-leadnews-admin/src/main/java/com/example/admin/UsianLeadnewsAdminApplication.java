package com.example.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UsianLeadnewsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsianLeadnewsAdminApplication.class, args);
    }

}
