package com.demon;

public class Test6 {
    private static final String LOCK = "lock";
    //总共只能存放full个
    private static final int full = 10;
    //count 剩余数量
    private int count = 0;

    public static void main(String[] args) {
      Test6 test6 =new Test6();
      for(int i=0;i<5;i++){
          new Thread(test6.new producer(),"生产者"+i).start();
          new Thread(test6.new consumer(),"消费者"+i).start();
      }
    }
    class producer implements Runnable{
        @Override
        public void run() {
            for(int i =0;i<10;i++){
                synchronized(LOCK) {
                    while (count == full) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ++count;
                    System.out.println("producer"+Thread.currentThread().getName()+"生产"+i);
                    LOCK.notifyAll();
                }

            }
        }
    }
    class consumer implements Runnable{
        @Override
        public void run() {
            for(int i =0;i<10;i++){
                synchronized(LOCK) {
                    while (count == 0) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    --count;
                    System.out.println("consumer"+Thread.currentThread().getName()+"消费"+i);
                    LOCK.notifyAll();
                }

            }
        }
    }
}
