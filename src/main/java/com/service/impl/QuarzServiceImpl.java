package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.LogDao;
import com.dao.QuarzDao;
import com.entity.Log;
import com.entity.Quarz;
import com.service.QuarzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuarzServiceImpl extends ServiceImpl<QuarzDao, Quarz> implements QuarzService {
    @Autowired
   private QuarzDao quarzDao;

   public int addQuarz(Quarz quarz){
        return quarzDao.addQuarz(quarz);
    }
    public List<Quarz> getQuarzList(Map<String,Object> params,Integer pageNum,Integer pageSize){
       return quarzDao.getQuarzList(params,pageNum,pageSize);
    }
    public int open(Quarz quarz){
       return quarzDao.open(quarz);
    }
    public int close(Quarz quarz){
        return quarzDao.close(quarz);
    }
}
