package com.dubbo.shop.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dubbo.shop.entity.TGoodsBase;
import com.dubbo.shop.entity.TGoodsInfo;
import com.dubbo.shop.mapper.TGoodsBaseMapper;
import com.dubbo.shop.mapper.TGoodsInfoMapper;
import com.dubbo.shop.service.ITGoodsBaseService;
import com.dubbo.shop.vo.GoodsVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-07-19
 */
@Component  // 声明这个组件是spring组件，受spring管理
@Service    // 发布服务，注意Service的包是dubbo
public class TGoodsBaseServiceImpl extends ServiceImpl<TGoodsBaseMapper, TGoodsBase> implements ITGoodsBaseService {


    @Resource // 类似@Autowired
    private TGoodsBaseMapper goodsBaseMapper;

    @Resource
    private TGoodsInfoMapper goodsInfoMapper;

    public PageInfo<TGoodsBase> page(int index, int size) {
        // 设置开始页码 和 每页数量
        PageHelper.startPage(index, size);

        List<TGoodsBase> items = goodsBaseMapper.list();

        // 分页，列表，分码个数【1,2,3  --  2,3,4】
        PageInfo<TGoodsBase> pageInfo = new PageInfo<TGoodsBase>(items, 3);

        return pageInfo;
    }

    public int add(GoodsVO vo) {
        // 添加基本信息，
        TGoodsBase goodsBase = vo.getGoodsBase();
        goodsBase.setCreateTime(LocalDateTime.now());
        goodsBase.setUpdateTime(LocalDateTime.now());
        goodsBaseMapper.insert(goodsBase); // mybatis plus 在 insert之后id会自动，填充到对应的entity中
        int goods_id = goodsBase.getId();

        // 添加描述信息
        TGoodsInfo goodsInfo = new TGoodsInfo();
        goodsInfo.setGoodsId(goods_id);
        goodsInfo.setInfo(vo.getGoodsInfo());
        goodsInfoMapper.insert(goodsInfo);

        return goods_id;
    }
}
