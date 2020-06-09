package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.LogDao;
import com.entity.Log;
import com.service.LogSerivce;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service("LogService")
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements LogSerivce{
    @Resource
    private LogDao logDao;
    @Override
    public int saveLog(Log log){
       return logDao.saveLog(log);
    }

}
