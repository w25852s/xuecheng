package com.xuecheng.cmsManage.consumer;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuecheng.cmsManage.service.CmsPageManageClientService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CmsPageConsumer {
    @Autowired
    private CmsPageManageClientService cmsPageManageClientService;


    @RabbitListener(queues = "${xuecheng.queue}")
    public void getMessage(String msg) {
        Map map = JSONObject.parseObject(msg, Map.class);

        String pageId = (String) map.get("pageId");

        cmsPageManageClientService.downLoadPage(pageId);

    }
}
