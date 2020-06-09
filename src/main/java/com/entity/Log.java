package com.entity;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Log extends Model<Log> implements Serializable {
    private Integer id;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;
    private String classMethod;
    private String httpMethod;
    private String params;
    //返回地址
    private String remoteAddr;
    //请求路径
    private String requestUrl;
    private String sessionId;
    private String title;
    private String username;
    private String response;
    private Long userTime;
    private String exception;

}
