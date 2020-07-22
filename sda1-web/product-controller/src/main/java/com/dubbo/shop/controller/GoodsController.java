package com.dubbo.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.shop.entity.TGoodsBase;
import com.dubbo.shop.service.ITGoodsBaseService;
import com.dubbo.shop.vo.GoodsVO;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("goods")
public class GoodsController {

    @Reference // dubbo引用注解，类似于@Autowired
    private ITGoodsBaseService goodsBaseService;

    @GetMapping("goods_list")
    public String goods_list(Model model){

        List<TGoodsBase> items = null;
        items = goodsBaseService.list();

        System.out.println(items==null);

        model.addAttribute("goods_list", items);
        return "goods_list";
    }

    // 分页
    @GetMapping("page/{pageIndex}/{pageSize}")
    public String page(Model model,
                       @PathVariable("pageIndex") int index,
                       @PathVariable("pageSize") int size){

        PageInfo<TGoodsBase> pageInfo = goodsBaseService.page(index, size);

        model.addAttribute("pageInfo", pageInfo);

        return "goods_list";
    }

    @PostMapping("add")
    public String add(GoodsVO vo){

        int goods_id = goodsBaseService.add(vo);

        return "redirect:/goods/page/1/1";
    }
}
