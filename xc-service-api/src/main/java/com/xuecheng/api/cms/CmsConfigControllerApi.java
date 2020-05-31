package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="cmsConfig 管理接口",description="cmsConfig管理接口，提供页面的增、删、改、查")

public interface CmsConfigControllerApi {

    @ApiOperation("通过主键 从 数据库 查询")
    public CmsConfig getConfigById(String id);

}
