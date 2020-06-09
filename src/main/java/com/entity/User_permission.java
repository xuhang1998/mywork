package com.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
@Data
public class User_permission extends Model<User_permission> implements Serializable {
    private Integer userId;
    private Integer permissionId;
}
