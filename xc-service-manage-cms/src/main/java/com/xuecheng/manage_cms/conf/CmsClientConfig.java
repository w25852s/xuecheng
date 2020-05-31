package com.xuecheng.manage_cms.conf;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class CmsClientConfig {

    //交换机的名称
    @Value("${xuecheng.rabbitmq.exchangeName}")
    private String exchangeName;

    @Bean
    public Exchange getExchange() {
        return ExchangeBuilder.directExchange(exchangeName).durable(true).build();
    }


}
