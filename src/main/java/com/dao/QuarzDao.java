package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Quarz;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QuarzDao extends BaseMapper<Quarz> {

    int addQuarz(@Param("q") Quarz quarz);
    List<Quarz> getQuarzList(@Param("map") Map<String,Object> params,@Param("num") Integer pageNum,@Param("size") Integer pageSize);
    int open(@Param("q") Quarz quarz);
    int close(@Param("q") Quarz quarz);
    Quarz selectById(@Param("id") Integer id);
}
