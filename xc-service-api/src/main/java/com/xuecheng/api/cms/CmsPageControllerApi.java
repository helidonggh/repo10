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

@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    //分页查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
        @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")})
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
    @ApiOperation("查询站点列表")
    public QueryResponseResult queryForSiteList();
    @ApiOperation("查询模板列表")
    public QueryResponseResult queryForTemplateList();
    @ApiOperation("添加功能")
    public CmsPageResult add(CmsPage cmsPage);
    @ApiOperation("根据id查询对象")
    public CmsPage get(String id);
    @ApiOperation("修改功能")
    public CmsPageResult edit(String id,CmsPage cmsPage);
    @ApiOperation("删除功能")
    public ResponseResult del(String id);
    @ApiOperation("发布页面")
    public ResponseResult post(String pageId);
}
