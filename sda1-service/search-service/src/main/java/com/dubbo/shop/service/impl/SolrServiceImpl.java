package com.dubbo.shop.service.impl;

import com.dubbo.shop.SolrService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

public class SolrServiceImpl implements SolrService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public void add() throws Exception{
        // solr里面的操作记录，document
        SolrInputDocument document = new SolrInputDocument();

        // 自定义域--要和和服务器配置文件对应
        document.setField("id", "100"); // 需要有一个唯一的表示
        document.setField("goods_name", "周杰伦演唱会门票，前排");
        document.setField("goods_price", "2999");
        document.setField("goods_sale_point", "90后的回忆");
        document.setField("goods_img", "暂无");

        // 提交
        solrClient.add(document);
        solrClient.commit();
    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public void select() {

    }
}
