package com.xuecheng.manage_cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitMQConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;

import com.xuecheng.manage_cms.service.PageService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

     @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if(queryPageRequest==null){
            queryPageRequest = new QueryPageRequest();
        }
        //创建值对象
        CmsPage cmsPage = new CmsPage();
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        if(page<=0){
            page = 1;
        }
        page = page -1;
        if(size<=0){
            size = 10;
        }
        Pageable pageable = new PageRequest(page,size);
        Page<CmsPage> pages = cmsPageRepository.findAll(example,pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(pages.getContent());
        queryResult.setTotal(pages.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
    //查询站点列表
    @Override
    public QueryResponseResult queryForSiteList() {
         QueryResult queryResult = new QueryResult();
         List<CmsSite> page = cmsSiteRepository.findAll();
         queryResult.setList(page);
         QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
         return queryResponseResult;
    }
    //查询模板列表
    @Override
    public QueryResponseResult queryForTemplateList() {
        QueryResult queryResult = new QueryResult();
        List<CmsTemplate> list = cmsTemplateRepository.findAll();
        queryResult.setList(list);
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
    //查询是否重复
    @Override
    public CmsPage findByPageNameAndPageWebPathAndSiteId(String pageName,String pageWebPath,String siteId) {
        return cmsPageRepository.findByPageNameAndPageWebPathAndSiteId(pageName, pageWebPath, siteId);
    }
    //添加
    @Override
    public CmsPage add(CmsPage cmsPage) {
        return cmsPageRepository.save(cmsPage);
    }
    //查询一个
    public CmsPage get(String id){
        Optional<CmsPage> option = cmsPageRepository.findById(id);
        if(option.isPresent()){
            return option.get();
        }
        return null;
    }
    //修改
    public CmsPageResult edit(String id, CmsPage cmsPage){
        CmsPage one = this.get(id);
        if(one!=null){
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新dataUrl
            one.setDataUrl(cmsPage.getDataUrl());
            CmsPage save = cmsPageRepository.save(one);
            return new CmsPageResult(CommonCode.SUCCESS,save);
        }else{
            return new CmsPageResult(CommonCode.FAIL,null);
        }
    }
    //删除
    public ResponseResult del(String id){
          //先删除
        Optional<CmsPage> option = cmsPageRepository.findById(id);
        if(option.isPresent()){
            cmsPageRepository.delete(option.get());
            return new ResponseResult(CommonCode.SUCCESS);
        }else{
            return new ResponseResult(CommonCode.FAIL);
        }
    }
    //页面静态化
    public String getPageHtml(String id){
         //1.获取模型数据
        Map model = getModel(id);
        //2.获取模板数据
        String template = getTemplate(id);
        //3.进行页面的静态化
        String s = generateHtml(template, model);
        return s;
    }
    @Autowired
    private RestTemplate restTemplate;//http服务,可以远程请求服务器，并封装结果
     //获取模型数据
    private Map getModel(String id){
        CmsPage cmsPage = this.get(id);
        if(cmsPage==null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataUrl = cmsPage.getDataUrl();
        if(dataUrl.isEmpty()){
            //根据页面的数据找不到dataurl
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    //获取模板数据
    private String getTemplate(String id){
        CmsPage cmsPage = this.get(id);
        if(cmsPage==null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取模板id
        String templateId = cmsPage.getTemplateId();
        if(templateId.isEmpty()){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //根据模板id获取模板文件id
        Optional<CmsTemplate> option = cmsTemplateRepository.findById(templateId);
        if(option.isPresent()){//如果存在
            CmsTemplate cmsTemplate = option.get();
            //获取模板文件的id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //取出模板文件的内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            try {
                String template = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
                return template;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    //进行页面静态化
    private String generateHtml(String template,Map model){
        //配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //模板加载器(string)
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",template);
        //设置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        try {
            //获取模板
            Template template1 = configuration.getTemplate("template");
            //将生成的静态页面转为string
            String s = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //页面发布
    public ResponseResult postPage(String pageId){
        String pageHtml = this.getPageHtml(pageId);
        if(StringUtils.isEmpty(pageHtml)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        this.saveToGriddfs(pageHtml, pageId);
        this.sendMQ(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //将静态页面存入gridfs
    public CmsPage saveToGriddfs(String htmlString,String pageId){
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();
        //存储之前先进行删除
        if(StringUtils.isNotEmpty(cmsPage.getHtmlFileId())){
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(cmsPage.getHtmlFileId())));
        }
           //进行存储
        InputStream inputStream = IOUtils.toInputStream(htmlString);
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        String fileId = objectId.toString();
        cmsPage.setHtmlFileId(fileId);
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }
    //发送MQ
    @Autowired
    RabbitTemplate rabbitTemplate;
    public void sendMQ(String pageId){
        CmsPage cmsPage = this.get(pageId);
        if(cmsPage==null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        Map<String,String> map = new HashMap<>();
        map.put("pageId",pageId);
        String message = JSON.toJSONString(map);
        String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_ROUTING_CMS_POSTPAGE,siteId,message);
    }
}
