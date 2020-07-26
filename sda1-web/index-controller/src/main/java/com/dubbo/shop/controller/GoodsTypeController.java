package com.dubbo.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.shop.entity.TGoodsType;
import com.dubbo.shop.service.ITGoodsTypeService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("goodsType")
public class GoodsTypeController {

    @Reference
    private ITGoodsTypeService goodsTypeService;

    @GetMapping("list")
    public String list(Model model){

        List<TGoodsType> items = goodsTypeService.list();

        model.addAttribute("items", items);

        return "index";
    }
}
