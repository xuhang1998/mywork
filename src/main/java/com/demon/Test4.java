package com.demon;




import cn.hutool.core.collection.CollUtil;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Test4 {
    public static void main(String[] args) {
        //数字2会被自动转换为字符串类型
        System.out.println(2+"5");
        String s = "dcd.dcs.gre.aa";
        String[] str = s.split("\\.");
        for(int i =0;i<str.length;i++){
        System.out.println(str[i]);}

    }
}
