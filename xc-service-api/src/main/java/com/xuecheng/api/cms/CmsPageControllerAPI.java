package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description="cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerAPI {
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
      @ApiImplicitParam(name="page",value="页 码",required=true,paramType="path",dataType="int"),
    @ApiImplicitParam(name="size",value="每页记录 数",required=true,paramType="path",dataType="int")
    })
public QueryResponseResult findList(Integer page, Integer size, QueryPageRequest request);
    @ApiOperation("新增页面")
    public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("根据Id查询")
    public CmsPage findById(String id);

    @ApiOperation("修改CmsPage属性")
    public CmsPageResult update(String id, CmsPage cmsPage);

    @ApiOperation("删除CmsPage")
    public ResponseResult delete(String id);

    @ApiOperation("页面持久话到 GridFs当中 并 通过rabbitmq 消息队列  发送消息 来进行 从 GridFs 中下载 页面")
    public ResponseResult postPage(String pageId);
}