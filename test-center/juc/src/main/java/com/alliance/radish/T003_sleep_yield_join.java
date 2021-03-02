package com.alliance.radish;

/**
 * 线程的几种状态及常用方法(区别)
 * 线程的状态：
 *  1-新建(New)：新创建的线程对象
 *  2-就绪(Ready/Runnable):执行线程的start方法，线程进入就绪状态，等待被调度器调用
 *  3-运行(Running):线程获得执行权，执行线程代码
 *  4-阻塞(Blocked):线程因为某种原因(主动/被动)放弃执行权，暂停执行程序。直到线程进入就绪状态，才有机会状态运行状态
 *  5-死亡(Teminated):线程执行完，或者因异常退出
 *阻塞的分类：
 *  1-等待阻塞:执行线程的wait()方法，线程释放所占资源，jvm将线程放入“等待池”。需要其他线程调用notify()或notifyAll()方法才能被唤醒
 *  2-同步阻塞：线程获取锁失败（锁被其他线程占用），线程进入同步阻塞状态。
 *  3-其他阻塞：运行的线程执行sleep()或join()方法，或者发出了I/O请求时，JVM会把该线程置为阻塞状态。当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新转入就绪状态。
 *
 * sleep():让线程暂时休息一段时间,时间一到自动恢复,不释放锁；线程进入阻塞状态
 * wait():会释放锁,需要被notify/notifyAll唤醒，线程进入阻塞状态；该方法要在同步方法或者同步代码块中才使用的
 *      |--notify()：唤醒当前线程，进入运行状态。该方法要在同步方法或者同步代码块中才使用的
 * 　　 |--notifyAll()：唤醒所有等待的线程。该方法要在同步方法或者同步代码块中才使用的
 * yield():暂停当前线程的执行，让出当前线程的执行权，当前线程进入就绪状态
 * join():等待该线程终止。如：t.join();表示等待t线程运行结束
 * stop():关闭线程，该方法已经@Deprecated，该方法易产生线程状态不一致，不建议使用
 */
public class T003_sleep_yield_join {
    public static void main(String[] args) {
        testSleep();
        testYield();
        testJoin();
        testWait();
    }

    /**
     * 线程的wait方法有两种形式
     * 1-接受毫秒数作为参数（会释放锁，与sleep不同；参数为0，则会一直等待），可通过notify，notifyAll或者等待的时间到期，令此线程接着执行
     * 2-不接受任何参数，这种wait()，将无限等待下去，直到线程接收到nofity或者notifyAll的通知信息，才能继续执行
     */
    private static Thread t1 = null;
    static void testWait(){
        t1 = new Thread(()->{
            for(int i = 0;i<50;i++){
                System.out.println("线程A--" + Thread.currentThread().getName() + "--" + i);
                try {
                    Thread.sleep(200);
                    if(i == 10){
                        synchronized (t1){
                            System.out.println("线程A挂起");
                            t1.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        new Thread(()->{
            for(int i=0;i<100;i++){
                System.out.println("线程B--" + Thread.currentThread().getName() + "--" + i);
                Object o = new Object();
                synchronized (o) {
                    try {
                        Thread.sleep(200);
                        if(i == 50){
                            synchronized (t1){
                                t1.notify();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 线程sleep后，并不释放所，所以当A线程执行完sleep后，B线程的Thread.currentThread().getName()永远都与A线程的Thread.currentThread().getName()不同
     * 当A线程获得调度执行权时，A的Thread.currentThread().getName()还是原来的
     */
    static void testSleep(){
        new Thread(()->{
            for(int i=0;i<100;i++){
                System.out.println("线程A--" + Thread.currentThread().getName() + "--" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            for(int i=0;i<100;i++){
                System.out.println("线程B--" + Thread.currentThread().getName() + "--" + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * yield让出当前线程执行权，线程进入就绪状态
     */
    static void testYield(){
        new Thread(()->{
            for(int i=0;i<100;i++){
                System.out.println("A线程-" + i);
                if(i%10 == 0){
                    Thread.yield();
                }
            }
        }).start();

        new Thread(()->{
            for(int i=0;i<100;i++){
                System.out.println("B线程-" + i);
                if(i%10 == 0){
                    Thread.yield();
                }
            }
        }).start();
    }

    /**
     * B线程等待A线程执行完再执行
     */
    static void testJoin(){
        class T1 extends Thread{
            public void run(){
                for(int i=0;i<100;i++){
                    System.out.println("A线程 - " + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Thread t1 = new T1();

        Thread t2 = new Thread(()->{
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0;i<100;i++){
                System.out.println("B线程 - " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
    }

}
