package com.alliance.radish;

/**
 * 线程状态测试
 */
public class T004_thread_state {

    private static volatile Class T1;
    private static Thread t2;

    public static class T1 extends Thread{
        @Override
        public void run() {
            for(int i=0;i<100;i++){
                System.out.println(Thread.currentThread().getName() + "--" + i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class T2 extends Thread{
        @Override
        public void run() {
            for(int i=0;i<1000;i++){
                System.out.println(Thread.currentThread().getName() + "--" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception{
        T1 t1 = new T1();
        System.out.println("new之后状态" + t1.getState());// NEW

        t1.start();
        System.out.println("start之后状态" + t1.getState()); //RUNNABLE

        t1.sleep(2000);
        System.out.println("sleep后状态" + t1.getState()); //TIMED_WAITING

        // 如果不加synchronized，会报错java.lang.IllegalMonitorStateException
        // 解释： 如果当前线程不是对象锁的持有者，该方法抛出一个java.lang.IllegalMonitorStateException 异常
        synchronized (t1){
            t1.wait(5000);
            System.out.println("wait(long)后状态" + t1.getState()); //TIMED_WAITING
            t1.notify();
        }
        synchronized (t1){
            t1.wait();
            System.out.println("wait后状态" + t1.getState()); //TERMINATED
            t1.notify();
        }

    }

}
