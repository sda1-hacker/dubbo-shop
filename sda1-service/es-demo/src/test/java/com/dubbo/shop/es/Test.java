package com.dubbo.shop.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.Index;
import org.junit.runner.RunWith;
import org.nlpcn.es4sql.SearchDao;
import org.nlpcn.es4sql.query.SqlElasticDeleteByQueryRequestBuilder;
import org.nlpcn.es4sql.query.SqlElasticSearchRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.reflect.annotation.ExceptionProxy;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Autowired
    TransportClient client;

    @Autowired
    SearchDao searchDao;

    // 创建索引，并添加内容
    @org.junit.Test
    public void createIndex() throws Exception{
        IndexResponse response = client.prepareIndex("person_index", "person_type", "1")
                .setSource(XContentFactory.jsonBuilder().startObject()
                .field("name", "sda1")
                .field("age", "18")
                .field("tel", "13333333333")
                .field("gender", "male")
                .endObject())
                .get();     // get() 表示执行

        System.out.println("索引名称" + response.getIndex() + "， 类型" + response.getType());

    }

    // 删除
    @org.junit.Test
    public void deleteIndex() throws Exception{
        DeleteResponse deleteResponse = client.prepareDelete("person_index", "person_type", "1").get();
        System.out.println(deleteResponse.getIndex() + ", " + deleteResponse.getType());
    }

    // 添加
    @org.junit.Test
    public void addDataToIndex() throws Exception{

        Map<String, String> dataMap = new HashMap<String, String>();

        dataMap.put("name", "huahua1");
        dataMap.put("age", "19");
        dataMap.put("tel", "15555555555");
        dataMap.put("gender", "female");

        String jsonData = JSON.toJSONString(dataMap);

        IndexResponse response = client.prepareIndex("person_index", "person_type", "3")
                .setSource(jsonData, XContentType.JSON)
                .get();

        System.out.println(response.getId());
    }

    @org.junit.Test
    public void updateIndexData(){
        Map<String, String> dataMap = new HashMap<String, String>();

        dataMap.put("name", "huahua1");
        dataMap.put("age", "19");
        dataMap.put("tel", "15777555555");
        dataMap.put("gender", "female");

        String jsonData = JSON.toJSONString(dataMap);

        UpdateResponse response = client.prepareUpdate("person_index", "person_type", "2")
                .setDoc(jsonData, XContentType.JSON)
                .get();

        System.out.println(response.getIndex());
    }

    @org.junit.Test
    public void queryData(){
        GetResponse response = client.prepareGet("person_index", "person_type", "2").get();
        System.out.println(response.getSourceAsString());
        
    }

    @org.junit.Test
    public void es_SQL_Test() throws Exception{
//        SearchDao searchDao = new SearchDao(client);
        String sql = "select * from person_index where 1 = 1 and tel = 15777555555";
        SqlElasticSearchRequestBuilder select = (SqlElasticSearchRequestBuilder)searchDao
                .explain(sql)
                .explain();

        Map res = (Map) JSON.parse((select.get()).toString());

        System.out.println(res);
    }
}
