package com.service.impl;
import com.dao.StockDao;
import com.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockDao stockDao;

    @Override
    public void decrByStock(String stockName) {
       int i = stockDao.findByName(stockName);
        if(i > 0 ){
            stockDao.updateByName(stockName);
        }

    }


    @Override
    public Integer selectByExample(String stockName) {
       return null;
    }
}


