package com.dubbo.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.shop.entity.TGoodsBase;
import com.dubbo.shop.mq.ProductMsgSender;
import com.dubbo.shop.service.ITGoodsBaseService;
import com.dubbo.shop.vo.GoodsVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class Test {


    @Reference // dubbo引用注解，类似于@Autowired
    private ITGoodsBaseService goodsBaseService;

    // 引入消息发送者
    @Resource
    private ProductMsgSender productMsgSender;

    @GetMapping("test")
    public List<TGoodsBase> Test(){
        List<TGoodsBase> items = null;
        items = goodsBaseService.list();
        return items;
    }

    @GetMapping("rabbitMQTest")
    public String rabbitMQTest(){
        TGoodsBase goodsBase = new TGoodsBase();
        goodsBase.setName("苹果手机~~");
        goodsBase.setPrice(10000L);
        goodsBase.setSalePoint("iphone10000");

        productMsgSender.sendMsg(goodsBase);

        return "添加完毕";
    }
}
