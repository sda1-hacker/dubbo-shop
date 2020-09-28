package com.dubbo.shop.mq;

import com.dubbo.shop.entity.TGoodsBase;
import com.dubbo.shop.mapper.TGoodsBaseMapper;
import com.dubbo.shop.mapper.TGoodsInfoMapper;
import com.dubbo.shop.vo.GoodsVO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ProductMsgRecv {

    @Resource // 类似@Autowired
    private TGoodsBaseMapper goodsBaseMapper;

    @RabbitListener(queues = "add_queue")
    public void handle(TGoodsBase goods){
        goodsBaseMapper.insert(goods);
    }

}
