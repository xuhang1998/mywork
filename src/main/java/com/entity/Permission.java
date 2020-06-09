package com.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
@Data
public class Permission extends Model<Permission> implements Serializable {
    private Integer id;
    private Integer pid;
    private String name;
    private String permission;
}
