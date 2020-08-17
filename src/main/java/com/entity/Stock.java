package com.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
@Data
public class Stock extends Model<Stock> implements Serializable {

    private Long id;

    private String stock_name;

    private int stock_num;
}
