package com.xuecheng.cmsManage.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageRepository  extends MongoRepository<CmsPage,String> {
}
