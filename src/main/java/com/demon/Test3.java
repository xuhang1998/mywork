package com.demon;

import java.util.Arrays;
//随机打乱数组
public class Test3 {
    public static void main(String[] args) {
        int[] a = {1,3,4,2,7};

        System.out.println(Arrays.toString(sort(a)));
    }
    public static int[] sort(int[] a){
        int[] b = new int[a.length];
        for(int i = 0; i < a.length;i++) {
            //随机获取下标
            int tmp = (int)(Math.random()*(a.length - i));
            //随机数:[ 0 ，a.length - i )
             b[i] = a[tmp];
            // 将此时a[tmp]的下标移动到靠后的位置
            int change = a[a.length - i - 1];
             a[a.length - i - 1] = a[tmp];
               a[tmp] = change;
             }
            return b;
    }
}
