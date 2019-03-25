package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/cms")
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private PageService pageService;

    @RequestMapping("/preview/{id}")
    public void preview(@PathVariable("id") String id){
        String pageHtml = pageService.getPageHtml(id);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(pageHtml.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
