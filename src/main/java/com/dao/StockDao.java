package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Stock;

public interface StockDao extends BaseMapper<Stock>{

    public int findByName(String stockname);

    public Integer updateByName(String stockname);
}
