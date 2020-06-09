package com.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class File extends Model<File> implements Serializable {

    private int id;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH-ss-mm")
    private Date createTime;
    private String type;
    private int size;
}
