package controller;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping("/freemarker")
public class FreemarkerTestController {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/getModel")
    public String getModel(Map<String,Object> map){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f", Map.class);
        Map map1 = forEntity.getBody();
        map.putAll(map1);
        return "index_banner";
    }

}
