package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LogDao extends BaseMapper<Log> {

    int saveLog(@Param("log") Log log);
    List<Log> selectLogAll(@Param("page") Integer page, @Param("limit")Integer limit);
    List<Log> listLog(@Param("map") Map<String,Object> map, @Param("page") Integer page, @Param("limit")Integer limit);
    int deleteById(@Param("id") Integer id);
}
