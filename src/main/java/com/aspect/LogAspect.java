package com.aspect;

import com.alibaba.fastjson.JSONObject;
import com.annotation.SysLog;
import com.dao.UserDao;
import com.entity.Log;
import com.entity.User;
import com.service.LogSerivce;
import com.utils.IpUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Order(5)
@Component
public class LogAspect {
    @Autowired
    private LogSerivce logSerivce;
    private ThreadLocal<Long> startTime = new ThreadLocal<>();
    private Log sysLog = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
    @Pointcut("@annotation(com.annotation.SysLog)")
    public void webLog(){}
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        startTime.set(System.currentTimeMillis());
        //接收到请求，记录请求的内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = (HttpSession) attributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        sysLog = new Log();
        sysLog.setStartTime(new Date());
        sysLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName()+","+joinPoint.getSignature().getName());
        sysLog.setHttpMethod(request.getMethod());
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for(int i = 0;i<args.length;i++){
            Object o = args[i];
            if((o instanceof ServletRequest) || (o instanceof ServletResponse) || (o instanceof MultipartFile)){
                args[i] = o.toString();
            }
        }
        String str = JSONObject.toJSONString(args);
        sysLog.setParams(str.length()>5000?JSONObject.toJSONString("请求参数数据过长不予显示"):str);
        String ip = IpUtils.getIpAddr(request);
        if("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)){
            ip = "127.0.0.1";

        }
        if(SecurityUtils.getSubject().getPrincipal() != null){
            sysLog.setUsername(((User)SecurityUtils.getSubject().getPrincipal()).getUsername());
        }
        sysLog.setRemoteAddr(ip);
        sysLog.setRequestUrl(request.getRequestURI().toString());
        if(session != null){
            sysLog.setSessionId(session.getId());
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog mylog = method.getAnnotation(SysLog.class);
        if(mylog != null){
            sysLog.setTitle(mylog.value());
        }

    }

    @AfterReturning(returning = "ret",pointcut = "webLog()")
    public void doAfterReturning(Object ret){

        String retString = JSONObject.toJSONString(ret);
        sysLog.setResponse(retString.length()>5000?JSONObject.toJSONString("请求参数数据过长不予显示"):retString);
        sysLog.setUserTime(System.currentTimeMillis() - startTime.get());

    }
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint)throws Throwable{
        Object object = proceedingJoinPoint.proceed();
        if(SecurityUtils.getSubject().getPrincipal() != null){
            sysLog.setUsername(((User)SecurityUtils.getSubject().getPrincipal()).getUsername());
        }
        logSerivce.saveLog(sysLog);
        return object;
    }


}

