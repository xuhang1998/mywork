package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.Log;

public interface LogSerivce extends IService<Log> {
    int saveLog(Log log);
}
