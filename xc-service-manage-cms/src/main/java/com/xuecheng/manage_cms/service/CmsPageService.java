package com.xuecheng.manage_cms.service;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.print.Pageable;

@Service
public class CmsPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;


    public QueryResponseResult findList( Integer page,  Integer size, QueryPageRequest request){
        if (request == null) {
            request = new QueryPageRequest();
        }

        CmsPage cmsPage = new CmsPage();
        if(StringUtils.isNotEmpty(request.getSiteId())){
            cmsPage.setSiteId(request.getSiteId());

        }
        if(StringUtils.isNotEmpty(request.getTemplateId())){
            cmsPage.setTemplateId(request.getTemplateId());

        }
        if(StringUtils.isNotEmpty(request.getPageAlias())){
            cmsPage.setPageAliase(request.getPageAlias());

        }


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("PageAlias", ExampleMatcher.GenericPropertyMatchers.contains());

        Example<CmsPage> example = Example.of(cmsPage, matcher);



        if (page <= 0) {
            page=1;
        }
        if (size <= 0) {
            size=10;
        }
        PageRequest pageable = PageRequest.of(page-1, size);
        Page<CmsPage> pages = cmsPageRepository.findAll(example,pageable);
        QueryResult queryResult=new QueryResult();
        queryResult.setList(pages.getContent());
        queryResult.setTotal(pages.getTotalElements());
        QueryResponseResult response = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return  response;


    }
}
