package com.xuecheng.cms.client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.cms.client.dao.CmsPageRepository;
import com.xuecheng.cms.client.service.PageService;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class ConsumerPostPage {
    @Autowired
    PageService pageService;
    @Autowired
    CmsPageRepository cmsPageRepository;

    @RabbitListener(queues = "${xuecheng.mq.queue}")
    public void postPage(String msg){
        Map map = JSON.parseObject(msg, Map.class);
        String pageId = (String) map.get("pageId");
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if(!optional.isPresent()){
            return;
        }
        pageService.savePageToServerPath(pageId);
    }
}
