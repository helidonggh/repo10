package daoTest;

import com.xuecheng.ManageCmsApplication;
import com.xuecheng.framework.domain.cms.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManageCmsApplication.class)
public class Test01 {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void test01(){
        Person person = new Person();
        person.setAge(20);
        person.setId(2);
        person.setName("张三");
        Person person1 = new Person();
        person1.setAge(20);
        person1.setId(3);
        person1.setName("张三");
        Person person2 = new Person();
        person2.setAge(20);
        person2.setId(4);
        person2.setName("张三");
        Person person3 = new Person();
        person3.setAge(20);
        person3.setId(5);
        person3.setName("张三");
        List list = new ArrayList();
        list.add(person);
        list.add(person1);
        list.add(person2);
        list.add(person3);
        mongoTemplate.insert(list,Person.class);
    }
    @Test
    public void test02(){
        Query query = new Query(new Criteria("id").is(1));
        Person one = mongoTemplate.findOne(query, Person.class);
        System.out.println(one);
    }

    @Test
    public void test03(){
        Query query = new Query(new Criteria("id").is(1));
        Person one = mongoTemplate.findOne(query, Person.class);
        System.out.println(one);
    }
}
