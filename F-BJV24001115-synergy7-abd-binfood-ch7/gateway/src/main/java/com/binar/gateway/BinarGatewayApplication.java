package com.binar.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BinarGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(BinarGatewayApplication.class, args);
	}

}
