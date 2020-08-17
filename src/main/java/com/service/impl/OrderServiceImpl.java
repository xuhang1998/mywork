package com.service.impl;

import com.dao.OrderDao;
import com.entity.t_Order;
import com.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;

    @Override
    public int createOrder(t_Order tOrder) {
       int i= orderDao.insertOrder(tOrder);
       return i;
    }
}


