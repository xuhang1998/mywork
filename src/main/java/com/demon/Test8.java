package com.demon;
//单例
public class Test8 {
    private  Test8 test8=new Test8();
    private Test8(){}
   /*public synchronized Test8 getInstance(){
       if(test8==null){
           return new Test8();
       }
       return test8;
   }*/
   public Test8 getTest8(){
       return test8;
   }
}

