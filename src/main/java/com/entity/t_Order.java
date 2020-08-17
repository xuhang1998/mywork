package com.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
@Data
public class t_Order extends Model<t_Order> implements Serializable {

    private Long id;

    private String order_name;

    private String order_user;
}
