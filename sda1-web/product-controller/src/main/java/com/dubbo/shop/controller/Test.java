package com.dubbo.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.shop.entity.TGoodsBase;
import com.dubbo.shop.service.ITGoodsBaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Test {


    @Reference // dubbo引用注解，类似于@Autowired
    private ITGoodsBaseService goodsBaseService;

    @GetMapping("test")
    public List<TGoodsBase> Test(){
        List<TGoodsBase> items = null;
        items = goodsBaseService.list();
        return items;
    }
}
