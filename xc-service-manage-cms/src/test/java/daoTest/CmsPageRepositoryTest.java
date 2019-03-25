package daoTest;

import com.xuecheng.ManageCmsApplication;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManageCmsApplication.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        for (CmsPage cmsPage : all) {
            System.out.println(cmsPage);
        }
    }

    @Test//测试分页查询
    public void testPage(){
        Pageable pageable = new PageRequest(0,10);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all.getContent());
        System.out.println(all.getTotalElements());
    }

    @Test
    public void testInsert(){
            //定义实体类
            CmsPage cmsPage = new CmsPage();
            cmsPage.setSiteId("s01");
            cmsPage.setTemplateId("t01");
            cmsPage.setPageName("测试页面");
            cmsPage.setPageCreateTime(new Date());
            List<CmsPageParam> cmsPageParams = new ArrayList<>();
            CmsPageParam cmsPageParam = new CmsPageParam();
            cmsPageParam.setPageParamName("param1");
            cmsPageParam.setPageParamValue("value1");
            cmsPageParams.add(cmsPageParam);
            cmsPage.setPageParams(cmsPageParams);
            cmsPageRepository.save(cmsPage);
            System.out.println(cmsPage);
    }

    @Test//测试修改方法
    public void testUpdate(){
        Optional<CmsPage> optional = cmsPageRepository.findById("5c80d853971abe0570b3d497");
        if(optional.isPresent()){//如果存在对象
            //取出对象
            CmsPage cmsPage = optional.get();
            //修改对象
            cmsPage.setPageAliase("黑马程序员");
            cmsPageRepository.save(cmsPage);
        }
    }
}
