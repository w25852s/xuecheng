package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")//扫描 model下的 实体类??
@ComponentScan(basePackages = "com.xuecheng.api.cms")//扫描api 下的 API接口
@ComponentScan(basePackages = "com.xuecheng.manage_cms")
public class CMSApplication {
    public static void main(String[] args) {
        SpringApplication.run(CMSApplication.class);
    }
}
