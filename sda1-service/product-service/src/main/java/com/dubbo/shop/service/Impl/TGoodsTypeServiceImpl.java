package com.dubbo.shop.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dubbo.shop.entity.TGoodsType;
import com.dubbo.shop.mapper.TGoodsTypeMapper;
import com.dubbo.shop.service.ITGoodsTypeService;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-07-19
 */
@Component  // 声明这个组件是spring组件，受spring管理
@Service    // 发布服务，注意Service的包是dubbo
public class TGoodsTypeServiceImpl extends ServiceImpl<TGoodsTypeMapper, TGoodsType> implements ITGoodsTypeService {

}
