package com.dao;

import com.entity.t_Order;
import org.apache.ibatis.annotations.Param;

public interface OrderDao{

    public int insertOrder(@Param("o") t_Order tOrder);
}
