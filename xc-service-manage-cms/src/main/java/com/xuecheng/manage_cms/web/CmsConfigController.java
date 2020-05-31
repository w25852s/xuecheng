package com.xuecheng.manage_cms.web;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cms/config")
public class CmsConfigController {

    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping("getModel/{id}")
    public CmsConfig getConfigById(@PathVariable("id") String id) {
        return  cmsPageService.getConfigById(id);
    }
}
