package test;

import controller.FreemarkerApplication;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FreemarkerApplication.class)
public class FreemarkerTest {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Test
    public void test01() throws FileNotFoundException {
        File file = new File("d:/index_banner.ftl");
        FileInputStream inputStream = new FileInputStream(file);
        ObjectId objectId = gridFsTemplate.store(inputStream, "index_banner.ftl");
        String s = objectId.toString();
        System.out.println(s);
    }
}
