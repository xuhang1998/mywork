package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.Quarz;

import java.util.List;
import java.util.Map;

public interface QuarzService extends IService<Quarz> {
    int addQuarz(Quarz quarz);
    List<Quarz> getQuarzList(Map<String,Object> params,Integer pageNum,Integer pageSize);
    int open(Quarz quarz);
    int close(Quarz quarz);
}
