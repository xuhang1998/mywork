package com.service.impl;

import com.config.RabbitMQConfig;
import com.entity.t_Order;
import com.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQOrderService {
    @Autowired
    private OrderService orderService;
    /**
     * 监听订单消息队列，并消费
     *
     * @param tOrder
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void createOrder(t_Order tOrder) {
        log.info("收到订单消息，订单用户为：{}，商品名称为：{}", tOrder.getOrder_user(), tOrder.getOrder_name());
        /**
         * 调用数据库orderService创建订单信息
         */
        orderService.createOrder(tOrder);
    }
}


