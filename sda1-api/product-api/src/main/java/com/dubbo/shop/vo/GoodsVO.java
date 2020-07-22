package com.dubbo.shop.vo;

import com.dubbo.shop.entity.TGoodsBase;

import java.io.Serializable;

public class GoodsVO implements Serializable {

    // 商品基本信息
    private TGoodsBase goodsBase;

    // 商品描述信息
    private String goodsInfo;

    public TGoodsBase getGoodsBase() {
        return goodsBase;
    }

    public void setGoodsBase(TGoodsBase goodsBase) {
        this.goodsBase = goodsBase;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
}
