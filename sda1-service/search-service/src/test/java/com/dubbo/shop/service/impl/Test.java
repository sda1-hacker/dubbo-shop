package com.dubbo.shop.service.impl;

import com.alibaba.dubbo.remoting.transport.ExceedPayloadLimitException;
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
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Resource
    private SolrClient solrClient;

    @Resource
    private TGoodsBaseMapper goodsBaseMapper;

    @org.junit.Test
    public void addTest() throws Exception {

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
//        solrClient.add("collection2", document);  // 指定添加到collection2
//        solrClient.commit("collection2");     // 指定提交到collection2
        solrClient.add(document);
        solrClient.commit();

    }


    @org.junit.Test
    public void queryTest() throws Exception {
        // 查询条件
        SolrQuery condition = new SolrQuery();
//        condition.setQuery("*:*"); // 查询字符串
//        condition.setQuery("goods_name:林俊杰周杰伦");
        condition.setQuery("goods_name:林俊杰");
        // 执行查询， 会将查询字符串进行分词，然后进行匹配查找
        QueryResponse response = solrClient.query(condition);
//        QueryResponse response1 = solrClient.query("collection2",condition);    // 从指定的collection中查询
        // 查询结果
        SolrDocumentList results = response.getResults();

        for (SolrDocument document : results) {
            System.out.println(document.get("id") + ", " +
                    document.get("goods_name") + ", " +
                    document.get("goods_sale_point"));
        }
    }

    @org.junit.Test
    public void queryHighlight() throws Exception {   // 查询结果高亮显示
        SolrQuery conditions = new SolrQuery();
        conditions.setHighlight(true);  // 开启高亮
        conditions.addHighlightField("goods_name"); // 未某个字段开启高亮，可以多少个
//        conditions.addHighlightField("goods_sale_point");

        // 添加高亮效果
        // 查询的结果会按照这样的效果显示
        // <font color='red'> 苹果手机 <font />
        conditions.setHighlightSimplePre("<font color='red'>");
        conditions.setHighlightSimplePost("<font />");

        // 设置查询条件，
        // 林俊杰 被设置成了高亮
        conditions.setQuery("goods_name:林俊杰");

        QueryResponse response = solrClient.query(conditions);

        // 获取高亮信息
        // 外层Map包含的内容：  String: id值，记录的高亮信息     , Map:    记录多个字段的高亮信息(可能是goods_nama，也可能是goods_sale_point)
        // 内层Map包含的内容：  String: 字段名称(例如：goods_name) , List:
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        // 当前记录的高亮信息
        Map<String, List<String>> record = highlighting.get("1001");
        // 某个具体字段的高亮信息
        List<String> goods_name = record.get("goods_name");
        System.out.println(goods_name.get(0));

        // 最后的输出结果如下，在前台页面直接显示就可以了
        // <font color='red'>林俊杰<font />演唱会门票，前排
    }

    @org.junit.Test
    public void deleteTest() throws Exception {
        solrClient.deleteById("100");   // id是精确匹配
        solrClient.deleteByQuery("goods_name:林俊杰"); // 分词，匹配，删除
        solrClient.commit();
    }

    // 将mysql中的数据，同步到solr中
    @org.junit.Test
    public void syncAllData() throws Exception {

        List<TGoodsBase> list = goodsBaseMapper.list();

        for (TGoodsBase temp : list) {
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
