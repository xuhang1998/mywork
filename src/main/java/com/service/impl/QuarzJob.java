package com.service.impl;

import com.utils.ApplicationContextUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class QuarzJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        UserServiceImpl userService = (UserServiceImpl) ApplicationContextUtil.getBean("UserService");
        //媒体定时任务
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Map<String,Object> parameter = (Map<String,Object>)dataMap.get("parameterList");
        String username = parameter.get("username").toString();
        String zt = parameter.get("zt").toString();
        try {
            userService.updateZt(username,zt);
        }catch (Exception e){
            try {
                shutdownJob(context);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * 把定时备份任务状态改为关闭
     */
    public void shutdownJob(JobExecutionContext context){
        try {
            context.getScheduler().shutdown();	//异常时关闭任务
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


