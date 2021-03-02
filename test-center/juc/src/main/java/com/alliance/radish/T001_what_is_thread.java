package com.alliance.radish;

import java.util.concurrent.TimeUnit;

/**
 * 什么是线程
 * run 和 start的区别
 * run(): 执行线程中的方法，同步执行
 * start(): 异步执行，调用线程中的run()方法
 */
public class T001_what_is_thread {

    private static class T1 extends Thread{
        public void run(){
            for (int i=0;i<10;i++){
                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T1");
            }
        }
    }

    public static void main(String[] args) {
        new T1().run();
//        new T1().start();
        for(int i=0;i<10;i++){
            try {
                TimeUnit.MICROSECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("main");
        }
    }

}
