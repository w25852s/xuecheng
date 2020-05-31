package com.xuecheng.manage_cms.web;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
public class CmsPagePreviewController  extends BaseController {

    @Autowired
    private CmsPageService pageService;


    @GetMapping("cms/page/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId) {
        String templateHtml = pageService.getTemplateHtml(pageId);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(templateHtml.getBytes("utf-8"));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
