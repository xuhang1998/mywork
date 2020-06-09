package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.LogDao;
import com.dao.UserDao;
import com.entity.Log;
import com.entity.User;
import com.service.UserService;
import com.utils.PageResult;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/log")
public class LogController{
    @Resource
    private LogDao logDao;
    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public String logList(){
        return "logList";
    }

    @ResponseBody
    @RequestMapping("/del")
    public R del(Log log){
        int id = log.getId();
        if(logDao.deleteById(id) != 0){
            return R.ok("删除成功");
        }else{
            return R.error("删除失败");
        }
    }

    @RequestMapping("/listData")
    @ResponseBody
    public PageResult<Log> listData(Integer page, Integer limit, Log log){
        PageResult<Log> result = new PageResult<>();
        List<Log> list;
        String username = log.getUsername();
        String title = log.getTitle();
        Integer id = log.getId();
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("title",title);
        map.put("id",id);
        if(id == null && title == null && username == null){
            list = logDao.selectLogAll((page-1)*limit,limit);
        }else{
            list = logDao.listLog(map,(page-1)*limit,limit);
        }
        result.setData(list);
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        int i = logDao.selectCount(queryWrapper);
        result.setCount(i);
        return result;

    }

}

