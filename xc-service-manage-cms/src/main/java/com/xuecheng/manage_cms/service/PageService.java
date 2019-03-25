package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

import java.util.Optional;

public interface PageService {
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    public QueryResponseResult queryForSiteList();

    public QueryResponseResult queryForTemplateList();

    public CmsPage findByPageNameAndPageWebPathAndSiteId(String pageName,String pageWebPath,String siteId);

    public CmsPage add(CmsPage cmsPage);

    public CmsPage get(String id);
    //修改
    public CmsPageResult edit(String id, CmsPage cmsPage);
    //删除
    public ResponseResult del(String id);
    //页面静态化
    public String getPageHtml(String id);
    //发布页面
    public ResponseResult postPage(String pageId);
}
