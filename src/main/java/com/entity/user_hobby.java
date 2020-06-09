package com.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data
public class user_hobby extends Model<user_hobby> implements Serializable {
    private Integer userId;
    private Integer hobbyId;
}
