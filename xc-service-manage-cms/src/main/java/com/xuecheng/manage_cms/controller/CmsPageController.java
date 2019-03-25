package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    private PageService pageService;
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return pageService.findList(page,size,queryPageRequest);
    }


    @GetMapping("/templateList")
    public QueryResponseResult queryForTemplateList(){
        return pageService.queryForTemplateList();
    }

    @GetMapping("/siteList")
    public QueryResponseResult queryForSiteList(){
        return pageService.queryForSiteList();
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        CmsPage cms = pageService.findByPageNameAndPageWebPathAndSiteId(cmsPage.getPageName(), cmsPage.getPageWebPath(), cmsPage.getSiteId());
        if(cms!=null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
            cmsPage.setPageId(null);
            CmsPage add = pageService.add(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,add);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsPage get(@PathVariable("id") String id) {
        return pageService.get(id);
    }

    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        return pageService.edit(id,cmsPage);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult del(@PathVariable("id") String id) {
        return pageService.del(id);
    }

    @Override
    @GetMapping("/post/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.postPage(pageId);
    }
}
