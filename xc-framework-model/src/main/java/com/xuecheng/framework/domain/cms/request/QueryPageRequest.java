package com.xuecheng.framework.domain.cms.request;


import lombok.Data;

@Data
public class QueryPageRequest {

    private String siteId;

    private String pageId;

    private String pageName;

    private String pageAlias;


    private String templateId;
}
