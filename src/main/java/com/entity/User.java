package com.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User extends Model<User> implements Serializable {
    private final static long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String telephone;
    private String email;
    private String hobby;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String sex;
    private String headImage;
    private String salt;
    /*
    * 状态
    * */
    private String zt;


}
