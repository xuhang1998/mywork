package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DSRW extends Model<DSRW> implements Serializable {
    /**
     * 自增主键
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private Long ID;
    /*
    * 定时任务名称
    * */
    private String DSRWMC;
    /*
    * 开始时间
    * */
    private Date  KSSJ;
    /*
    * 结束时间
    * */
    private Date JSSJ;
    /*
    * 开始日期
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date KSRQ;
    /*
    * 结束日期
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date JSRQ;
    /*
    * 定时任务状态
    * */
    private String DSRWZT;
}
