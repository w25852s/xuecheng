package com.xuecheng.manage_cms.web;

import com.xuecheng.api.cms.CmsPageControllerAPI;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cms/page")
@Log
public class CmsPageController implements CmsPageControllerAPI {
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") Integer page,@PathVariable("size")
            Integer size, QueryPageRequest request) {
log.info("进入Contorller");
        QueryResult<CmsPage> queryResult=new QueryResult<>();
        List<CmsPage> pages = new ArrayList<>();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("哈哈");
        pages.add(cmsPage);
        queryResult.setList(pages);
        queryResult.setTotal(1);
        QueryResponseResult response = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return response;
    }
}
