package com.demon;
//冒泡排序
public class Test7 {
    public static void main(String[] args) {
        int a[] = {1,3,2,5,4,7,6};
        for(int i=0;i<a.length;i++){
           for(int j=1;j<a.length-i-1;j++){
               if(a[j]>a[j+1]){
                   int temp;
                   temp = a[j];
                   a[j] = a[j+1];
                   a[j+1] = temp;
               }
           }
        }
        for(int i =0;i<a.length;i++){
        System.out.print(a[i]+" ");
        }
    }

}
