package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.QuarzDao;
import com.dao.UserDao;
import com.entity.Log;
import com.entity.Quarz;
import com.entity.User;
import com.service.QuarzService;
import com.service.impl.QuarzJob;
import com.utils.PageResult;
import com.utils.QuarzManager;
import com.utils.R;
import com.utils.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.date.DateUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/quarz")
public class QuarzController {

    @Autowired
    private QuarzService quarzService;
    @Resource
    private UserDao userDao;
    @Resource
    private QuarzDao quarzDao;
    private static String JOB_GROUP_NAME = "DS_JOBGROUP_NAME";            //任务组
    private static String TRIGGER_GROUP_NAME = "DS_TRIGGERGROUP_NAME";        //触发器组

    @RequestMapping("/index")
    public String list(){
        return "quarz";
    }
    @RequestMapping("/edit")
    public String edit(Model model,Integer id){
        /*model.addAttribute("id",id);*/
        Map<Integer,String> m = new HashMap<>();
        List<String> list = userDao.getAllName();
        for(int i = 0;i < list.size();i++){
            m.put(i,list.get(i));
        }
        model.addAttribute("list",m);
       Quarz quarz = quarzDao.selectById(id);
        model.addAttribute("quarz",quarz);
        return "editQuarz";
    }
    @RequestMapping("/add")
    public String add(Model model)
    {
        Map<Integer,String> m = new HashMap<>();
        List<String> list = userDao.getAllName();
        for(int i = 0;i < list.size();i++){
            m.put(i,list.get(i));
        }
        model.addAttribute("list",m);
        return "addQuarz";
    }
    @ResponseBody
    @RequestMapping("/remove")
    public R remove(Quarz quarz){
      Integer id = quarz.getId();
      QueryWrapper<Quarz> queryWrapper = new QueryWrapper<Quarz>();
      queryWrapper.eq("id",id);
      this.removeJob(quarz.getDSRWMC()+"s");
      this.removeJob(quarz.getDSRWMC()+"t");
        if(quarzDao.delete(queryWrapper) != 0){
            return R.ok("删除成功");
        }else{
            return R.error("删除失败");
        }
    }
    @ResponseBody
    @RequestMapping("/addQuarz")
    public R addQuarz(Quarz quarz){
        //秒
        String startS = quarz.getKSSJ().split(":")[2];
        //分
        String startM = quarz.getKSSJ().split(":")[1];
        // 时
        String startH = quarz.getKSSJ().split(":")[0];
        //日
        String startDay = DateUtil.format(quarz.getKSRQ(),"yyyy-MM-dd").split("-")[2];
        //月
        String startMonth = DateUtil.format(quarz.getKSRQ(),"yyyy-MM-dd").split("-")[1];
        //年
        String startYear = DateUtil.format(quarz.getKSRQ(),"yyyy-MM-dd").split("-")[0];
        String startCron = startS+" "+startM+" "+startH+" "+startDay+" "+startMonth+" "+"? "+startYear;
        String startDSRWMC = quarz.getDSRWMC()+"s";

        if (quarz.getZT().equals("1")){
            this.addJob(startDSRWMC, startCron,quarz.getUsername(),"1");//添加任务
        }
        /*关闭*/
        //秒
        String endS = quarz.getJSSJ().split(":")[2];
        //分
        String endM = quarz.getJSSJ().split(":")[1];
        // 时
        String endH = quarz.getJSSJ().split(":")[0];
        //日
        String endDay = DateUtil.format(quarz.getJSRQ(),"yyyy-MM-dd").split("-")[2];
        //月
        String endMonth = DateUtil.format(quarz.getJSRQ(),"yyyy-MM-dd").split("-")[1];
        //年
        String endYear = DateUtil.format(quarz.getJSRQ(),"yyyy-MM-dd").split("-")[0];
        String endCron = endS+" "+endM+" "+endH+" "+endDay+" "+endMonth+" "+"? "+endYear;
        String endDSRWMC = quarz.getDSRWMC()+"t";
        if (quarz.getZT().equals("1")){
            this.addJob(endDSRWMC, endCron,quarz.getUsername(),"0");//添加任务
        }
        if((quarzService.addQuarz(quarz))!=0){
            return R.ok("保存成功");
        }else{
            return R.error("保存失败");
        }
    }
    @RequestMapping("/listData")
    @ResponseBody
    public TableDataInfo list(HttpServletRequest request, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize)
    {
        TableDataInfo dataInfo = new TableDataInfo();
        List<Quarz> list;
        Map<String, Object> params = new HashMap<>();
        params.put("username",request.getParameter("username"));
        params.put("DSRWMC",request.getParameter("DSRWMC"));
        params.put("ZT",request.getParameter("ZT"));
        params.put("KSRQ",request.getParameter("KSRQ"));
        params.put("JSRQ",request.getParameter("JSRQ"));
        pageNum = pageNum == null ? 0 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        list = quarzService.getQuarzList(params,pageNum,pageSize);
        dataInfo.setRows(list);
        QueryWrapper<Quarz> queryWrapper = new QueryWrapper<>();
        int i =quarzDao.selectCount(queryWrapper);
        dataInfo.setTotal(i);
        return dataInfo;

    }
    @ResponseBody
    @RequestMapping("/open")
    public R open(Quarz quarz){
        if((quarzService.open(quarz))!=0){
            return R.ok("开启成功");
        }else{
            return R.error("开启失败");
        }
    }
    @ResponseBody
    @RequestMapping("/close")
    public R close(Quarz quarz){
        if((quarzService.open(quarz))!=0){
            return R.ok("关闭成功");
        }else{
            return R.error("关闭失败");
        }
    }
    /**新增任务
     * @param JOBNAME	任务名称
     * @param FHTIME 	时间规则
     * @param  username 用户名
     * @param zt 状态
     */
    public void addJob(String JOBNAME, String FHTIME,String username,String zt){
        Map<String,Object> parameter = new HashMap<String,Object>();
        parameter.put("username",username);
        parameter.put("zt",zt);
        QuarzManager.addJob(JOBNAME,JOB_GROUP_NAME, JOBNAME, TRIGGER_GROUP_NAME, QuarzJob.class, FHTIME ,parameter);
    }
    /**删除任务
     * @param JOBNAME
     */
    public void removeJob(String JOBNAME){
        QuarzManager.removeJob(JOBNAME, JOB_GROUP_NAME,JOBNAME, TRIGGER_GROUP_NAME);
    }
}
