package com.xuecheng.manage_cms.service;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.netty.util.internal.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class CmsPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private CmsTemplateRepository templateRepository;


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
        if (cmsPage == null) {
            //抛出 非法参数异常

        }


        CmsPage pageFind = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if (pageFind != null) {
            // 已存在 抛出异常
            //throw new CustomerException(CommonCode.FAIL);
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }



            // 数据库不存在 则 插入
            cmsPage.setPageId(null);
            CmsPage pageSave = cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);



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

    /**
     * 通过 id  查询 轮播图 数据库 内容     *
     */
    public CmsConfig getConfigById(String id) {
        Optional<CmsConfig> cmsConfig = cmsConfigRepository.findById(id);
        if (cmsConfig.isPresent()) {
            CmsConfig cmsConfig1 = cmsConfig.get();
            return cmsConfig1;
        }

        return  null;

    }


    //模板静态化

    public String getTemplateHtml(String cmsPagId) {

        //获得 模板
        Map model = getModel(cmsPagId);

        //获取template
        String template=getTemplate(cmsPagId);
        if (StringUtils.isEmpty(template)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //模板静态化
        String templateHtml = generateHtml(model, template);
        if (StringUtils.isEmpty(template)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return templateHtml;
    }
        //模板静态化 步骤三 降模板与模型惊醒组装
    private String generateHtml(Map model, String template) {
        Configuration configuration = new Configuration(Configuration.getVersion());

        StringTemplateLoader loader = new StringTemplateLoader();

        loader.putTemplate("template", template);

        configuration.setTemplateLoader(loader);

        try {
            Template template1 = configuration.getTemplate("template", "utf-8");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // 静态话 步骤2  获取 template
    private String getTemplate(String cmsPagId) {
        CmsPage cmsPage = getCmsPageById(cmsPagId);

        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> cmsTemplateOptional = templateRepository.findById(templateId);
        CmsTemplate template=null;
        if (cmsTemplateOptional.isPresent()) {
            template = cmsTemplateOptional.get();
        }
        if (template == null) {
            ExceptionCast.cast(CmsCode.CMS_TEMPLATENOTEXISTS);
        }

        //获取到模板ID后
        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(cmsPage.getHtmlFileId())));
        if (file == null) {
            ExceptionCast.cast(CmsCode.CMS_FILEDNOTEXISTS);
        }
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(file.getObjectId());

        GridFsResource resource = new GridFsResource(file,gridFSDownloadStream);

        try {
            return IOUtils.toString(resource.getInputStream(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    //根据pageId 获取page
    private CmsPage getCmsPageById(String cmsPageId) {
        Optional<CmsPage> cmsPageOptional = cmsPageRepository.findById(cmsPageId);
        CmsPage cmsPage=null;
        if (cmsPageOptional.isPresent()) {
            cmsPage = cmsPageOptional.get();
        }
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGENOTEXISTS);
        }
        return cmsPage;
    }

    // 静态话 步骤1  获取模型
    private Map getModel(String cmsPageId) {

        CmsPage cmsPage = getCmsPageById(cmsPageId);

        String dataUrl = cmsPage.getDataUrl();


        if (StringUtils.isEmpty(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        //远程 获取 model

        ResponseEntity<Map> model = restTemplate.getForEntity(dataUrl, Map.class);
        if (model == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        return model.getBody();


    }

}
