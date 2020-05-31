package com.xuecheng.cmsManage.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmsClientConfig {
    //队列bean的名称
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";
    //交换机的名称
    public static final String EX_ROUTING_CMS_POSTPAGE="ex_routing_cms_postpage";

    @Value("${xuecheng.queue}")
    private String queueName;

    @Value("${xuecheng.siteId}")
    private String siteId;
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue getCmsQueue() {
        return new Queue(queueName);
    }

    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange getExchange() {
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    @Bean
    public Binding bindQueueToExchange(@Qualifier(QUEUE_CMS_POSTPAGE) Queue queue,@Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(siteId).noargs();
    }
}
