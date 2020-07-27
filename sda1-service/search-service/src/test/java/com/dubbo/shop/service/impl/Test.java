package com.dubbo.shop.service.impl;

import com.dubbo.shop.entity.TGoodsBase;
import com.dubbo.shop.entity.TGoodsInfo;
import com.dubbo.shop.mapper.TGoodsBaseMapper;
import com.dubbo.shop.mapper.TGoodsInfoMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Resource
    private SolrClient solrClient;

    @Resource
    private TGoodsBaseMapper goodsBaseMapper;

    @org.junit.Test
    public void addTest() throws Exception{

        // solr里面的操作记录  -- 也就是一条数据
        SolrInputDocument document = new SolrInputDocument();

        // 自定义域--要和和服务器配置文件对应
        document.setField("id", "1001"); // id在服务器的配置文件中不需要配置。但插入数据的时候需要写。
        // 如果id存在则执行修改操作
        document.setField("goods_name", "林俊杰演唱会门票，前排");
        document.setField("goods_price", "5999");
        document.setField("goods_sale_point", "90后的回忆");
        document.setField("goods_img", "暂无");

        // 提交
        solrClient.add(document);
        solrClient.commit();

    }


    @org.junit.Test
    public void queryTest() throws Exception{
        // 查询条件
        SolrQuery condition = new SolrQuery();
//        condition.setQuery("*:*"); // 查询字符串
//        condition.setQuery("goods_name:林俊杰周杰伦");
        condition.setQuery("goods_name:林俊杰");
        // 执行查询， 会将查询字符串进行分词，然后进行匹配查找
        QueryResponse response = solrClient.query(condition);
        // 查询结果
        SolrDocumentList results = response.getResults();

        for (SolrDocument document : results){
            System.out.println(document.get("id") + ", " +
            document.get("goods_name") + ", " +
            document.get("goods_sale_point"));
        }
    }

    @org.junit.Test
    public void deleteTest() throws Exception{
        solrClient.deleteById("100");   // id是精确匹配
        solrClient.deleteByQuery("goods_name:林俊杰"); // 分词，匹配，删除
        solrClient.commit();
    }

    // 将mysql中的数据，同步到solr中
    @org.junit.Test
    public void syncAllData() throws Exception{

        List<TGoodsBase> list = goodsBaseMapper.list();

        for(TGoodsBase temp : list){
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id", temp.getId());
            document.setField("goods_name", temp.getName());
            document.setField("goods_price", temp.getPrice());
            document.setField("goods_sale_point", temp.getSalePoint());
            document.setField("goods_img", temp.getImages());

            solrClient.add(document);

        }

        solrClient.commit();


    }

}
