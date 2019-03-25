package com.xuecheng.cms.client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.cms.client.dao.CmsPageRepository;
import com.xuecheng.cms.client.dao.CmsSiteRepository;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;
    //实现步骤
    //1.根据fileid从Griddfs下载文件
    public void savePageToServerPath(String pageId){
        //1.查询文件
        Optional<CmsPage> option = cmsPageRepository.findById(pageId);
        if(!option.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = option.get();
        //获取存储的物理路径
        String pagePhysicalPath = cmsPage.getPagePhysicalPath();
        String pageName = cmsPage.getPageName();
        CmsSite cmsSite = getCmsSiteById(cmsPage.getSiteId());
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        String path = sitePhysicalPath + pagePhysicalPath+ pageName;
        System.out.println(path);
        //获取流对象
        InputStream inputStream = selectFileStream(cmsPage.getHtmlFileId());
        if(inputStream==null){
            return;
        }
            FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream= new FileOutputStream(new File(path));
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //查询站点的对象
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    public CmsSite getCmsSiteById(String siteId){
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if(optional.isPresent()){
            CmsSite cmsSite = optional.get();
            return cmsSite;
        }
            return null;
    }
    //根据文件的id查询文件信息
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    public InputStream selectFileStream(String fileId){
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        ObjectId objectId = gridFSFile.getObjectId();
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(objectId);
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
