package test;

import com.xuecheng.search.SearchApplication;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class Test01 {

    @Autowired
    RestHighLevelClient client;
    @Test
    public void test01() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("test_02");
        createIndexRequest.settings(Settings.builder().put("number_of_shards",1)
                .put("number_of_replicas",0));
        //设置映射
        createIndexRequest.mapping("doc"," {\n" +
                " \t\"properties\": {\n" +
                " \"name\": {\n" +
                " \"type\": \"text\",\n" +
                " \"analyzer\":\"ik_max_word\",\n" +
                " \"search_analyzer\":\"ik_smart\"\n" +
                " },\n" +
                " \"description\": {\n" +
                " \"type\": \"text\",\n" +
                " \"analyzer\":\"ik_max_word\",\n" +
                " \"search_analyzer\":\"ik_smart\"\n" +
                " },\n" +
                " \"studymodel\": {\n" +
                " \"type\": \"keyword\"\n" +
                " },\n" +
                " \"price\": {\n" +
                " \"type\": \"float\"\n" +
                " }\n" +
                " }\n" +
                "}", XContentType.JSON);
        //创建索引操作客户端
        IndicesClient indices = client.indices();
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    @Test
    public void test02() throws IOException {
        //准备json数据
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("description", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud门 3.实战Spring Boot 4.注册中心eureka。");
        jsonMap.put("studymodel", "201001");
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
        jsonMap.put("timestamp", dateFormat.format(new Date()));
        jsonMap.put("price", 5.6f);
         //索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course","doc");
        indexRequest.source(jsonMap);
        //索引响应对象
        IndexResponse indexResponse = client.index(indexRequest);
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);

    }
}
