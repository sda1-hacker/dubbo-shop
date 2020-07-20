package com.dubbo.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dubbo.shop.entity.TGoodsBase;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-07-19
 */
public interface ITGoodsBaseService extends IService<TGoodsBase> {

    PageInfo<TGoodsBase> page(int index, int size);
}
