package com.xuecheng.cms.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.xuecheng.framework")
@EntityScan("com.xuecheng.framework.domain")
@ComponentScan(basePackages = "com.xuecheng.cms.client")
public class ManageCmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class,args);
    }
}
