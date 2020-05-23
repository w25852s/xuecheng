package com.xuecheng.framework.domain.cms.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPageRequest {
    @ApiModelProperty("站点id")
    private String siteId;

    private String pageId;

    private String pageName;

    private String pageAlias;


    private String templateId;
}
