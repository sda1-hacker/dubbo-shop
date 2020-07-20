package com.dubbo.shop.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.dubbo.shop.entity.TGoodsBase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-07-19
 */
public interface TGoodsBaseMapper extends BaseMapper<TGoodsBase> {

    public List<TGoodsBase> list();

    public List<TGoodsBase> findByConditions(TGoodsBase t);
}
