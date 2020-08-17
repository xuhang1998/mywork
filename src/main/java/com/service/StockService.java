package com.service;

public interface StockService {

     void decrByStock(String stockName);

     Integer selectByExample(String stockName);
}
