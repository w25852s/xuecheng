package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.xuecheng.framework")
@ComponentScan("com.xuecheng.cmsManage")
public class CmsManageClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsManageClientApplication.class);
    }
}
