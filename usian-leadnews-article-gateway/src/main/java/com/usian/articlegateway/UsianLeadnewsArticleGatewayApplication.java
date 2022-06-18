package com.usian.articlegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UsianLeadnewsArticleGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsianLeadnewsArticleGatewayApplication.class, args);
	}

}
