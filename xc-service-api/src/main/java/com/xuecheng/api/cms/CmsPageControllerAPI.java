package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

public interface CmsPageControllerAPI {

    public QueryResponseResult findList(Integer page, Integer size, QueryPageRequest request);
}