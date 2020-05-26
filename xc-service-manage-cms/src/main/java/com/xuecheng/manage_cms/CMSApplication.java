package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")//扫描 model下的 实体类??
@ComponentScan(basePackages = "com.xuecheng.api")//扫描api 下的 API接口
@ComponentScan(basePackages = "com.xuecheng.manage_cms")//扫描本模块的包
@ComponentScan(basePackages = "com.xuecheng.framework")//扫描common 模块下的包
public class CMSApplication {
    public static void main(String[] args) {
        SpringApplication.run(CMSApplication.class);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

}
