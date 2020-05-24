package com.xuecheng.manage_cms.service;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
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
import java.util.Optional;

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



    public CmsPageResult add(CmsPage cmsPage) {

        CmsPage pageFind = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());

        if (pageFind == null) {
            // 数据库不存在 则 插入
            cmsPage.setPageId(null);
            CmsPage pageSave = cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }

        return new CmsPageResult(CommonCode.FAIL, null);
    }


    public CmsPage findById(String id) {
        Optional<CmsPage> page = cmsPageRepository.findById(id);
        if (page.isPresent()) {
            CmsPage cmsPage = page.get();
            return cmsPage;
        }
    return null;

    }

    public CmsPageResult update(String id, CmsPage cmsPage) {
        //通过id 查询是否存在
        CmsPage one = findById(id);
        if (one != null) {
            one.setPageAliase(cmsPage.getPageAliase());
            one.setPageName(cmsPage.getPageName());
            one.setSiteId(cmsPage.getSiteId());
            one.setTemplateId(cmsPage.getTemplateId());
            one.setPageWebPath(cmsPage.getPageWebPath());
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            one.setDataUrl(cmsPage.getDataUrl());
            CmsPage save = cmsPageRepository.save(one);

            return new CmsPageResult(CommonCode.SUCCESS, save);

        }

        return new CmsPageResult(CommonCode.FAIL, null);

    }

    public ResponseResult delete(String id) {
        Optional<CmsPage> option = cmsPageRepository.findById(id);
        if (option.isPresent()) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
