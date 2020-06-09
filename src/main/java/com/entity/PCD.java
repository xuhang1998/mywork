package com.entity;

import lombok.Data;
@Data
/*三级联动地址表*/
public class PCD {

    private int id;
    private int pid;
    private String name;

    @Override
    public String toString() {
        return "PCD [id=" + id + ", pid=" + pid + ", name=" + name + "]";
    }

}
