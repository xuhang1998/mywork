package com.demon;




import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test2 {
    public static void main(String[] args) {
        List list = new ArrayList<Object>();

        list.add("1");
        list.add("2");
        list.add("14");
        list.add("13");
        List list2 = new ArrayList<Object>();
        list2.add(1);
        list2.add(2);
        list2.add(14);
        list2.add(13);
        List ll = Test2.sort(list);
        Collections.sort(ll);
        List ll2= Test2.sort(list2);
        Collections.sort(ll2);
        System.out.println(ll);
        System.out.println(ll2);

    }
     static List sort(List list){
         Object[] obj= list.toArray();
         List l = new ArrayList();
         for(int i=0;i<=obj.length-1;i++){
             if(obj[i] instanceof String){
                 l.add(Integer.parseInt(obj[i].toString()));
             }else{
             l.add(obj[i]);}
         }
          return l;
     }

}
