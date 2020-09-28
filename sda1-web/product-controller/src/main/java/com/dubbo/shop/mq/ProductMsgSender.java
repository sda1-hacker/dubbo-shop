package com.dubbo.shop.mq;

import com.dubbo.shop.entity.TGoodsBase;
import com.dubbo.shop.vo.GoodsVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProductMsgSender {

    public static final String PRODUCT_EXCHANGE_NAME_ADD = "add_exchange";

    @Autowired
    private RabbitTemplate template;

    public void sendMsg(TGoodsBase goods){
        template.convertAndSend(PRODUCT_EXCHANGE_NAME_ADD, "add_product", goods);
    }

}
