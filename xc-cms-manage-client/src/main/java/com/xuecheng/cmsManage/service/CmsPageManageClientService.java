package com.xuecheng.cmsManage.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.cmsManage.dao.CmsPageRepository;
import com.xuecheng.cmsManage.dao.CmsSiteRepository;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service

public class CmsPageManageClientService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;
    public void downLoadPage(String pageId) {
        Optional<CmsPage> cmsPageOptional = cmsPageRepository.findById(pageId);
        CmsPage cmsPage = null;
        if (cmsPageOptional.isPresent()) {
            cmsPage = cmsPageOptional.get();
        }
        //如果未找到 抛异常

        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGENOTEXISTS);
        }
        //获取 fileId
        String htmlFileId = cmsPage.getHtmlFileId();

        String siteId = cmsPage.getSiteId();

        Optional<CmsSite> cmsSiteOptional = cmsSiteRepository.findById(siteId);
        CmsSite cmsSite = null;
        if (cmsSiteOptional.isPresent()) {
            cmsSite = cmsSiteOptional.get();
        }
        //如果未找到 抛异常

        if (cmsSite == null) {
            ExceptionCast.cast(CmsCode.CMS_FILEDNOTEXISTS);
        }


        //获取文件输入流
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));

        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());

        GridFsResource resource = new GridFsResource(gridFSFile, gridFSDownloadStream);

        // 文件 物理 路径
        String filePath = cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream  = new FileOutputStream(new File(filePath));
            inputStream  = resource.getInputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}
