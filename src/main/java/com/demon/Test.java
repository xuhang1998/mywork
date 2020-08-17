package com.demon;


import java.util.*;
/*
判断字符串中字符重复的个数
*/
public class Test {
    public static void main(String[] args) {
        String s = "123fnhtuff332lfh";
         char[] ch = s.toCharArray();
        Set set = new HashSet();
         Map<Object,Integer> m = new HashMap<>();
         for(int i =0;i<ch.length;i++){
            set.add(ch[i]);
         }
         Object[] cset = set.toArray();
             for(int j=0;j<cset.length;j++){
                 int p=0;
                 for(int i=0;i<ch.length;i++){
                     if(cset[j].equals(ch[i])){
                         p++;
                         m.put(cset[j],p);
                     }
             }
         }
             System.out.println(m.toString());
             Integer i1 = 100;
             Integer i2 = 100;

             Integer i3 = 200;
             Integer i4 = 200;
            /*public static Integer valueOf(int i) {
            if (i >= IntegerCache.low && i <= IntegerCache.high)
           //如果数值在-128~127之间的
        return IntegerCache.cache[i + (-IntegerCache.low)];
           //返回
            return new Integer(i);
         }*/
            //所以数值在-128到127之间可以直接得到
             System.out.println(i1==i2);
             //200大于127重新new,所以不相等
             System.out.println(i3==i4);

    }
}
