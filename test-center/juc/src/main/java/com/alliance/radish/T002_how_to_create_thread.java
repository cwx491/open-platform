package com.alliance.radish;

import java.util.concurrent.*;

/**
 * java多线程实现的四种方式
 * 1、继承Thread类，重写run方法（其实Thread类本身也实现了Runnable接口）
 * 2、实现Runnable接口，重写run方法
 * 3、实现Callable接口，重写call方法（有返回值）
 * 4、使用线程池（有返回值）
 */
public class T002_how_to_create_thread {

    /**
     * 无返回值
     * 1.继承Thread类，重写run方法
     * 通过new T1().start();启动或者new Thread(new T1()).start();启动
     * Thread构造参数需要Runnable类型，因为Thread实现了Runnable接口，所以可以使用new Thread(new T1()).start();启动
     */
    public static class T1 extends Thread{
        public void run(){
            System.out.println("方式一：继承Thread创建线程");
        }
    }

    /**
     * 无返回值
     * 实现Runnable接口，重写run方法，实现Runnable接口的实现类的实例对象作为Thread构造函数的target
     * 通过 new Thread(new T2()).start();启动
     */
    public static class T2 implements Runnable{
        @Override
        public void run() {
            System.out.println("方式二：实现Runnable创建线程");
        }
    }


    /**
     * jdk1.5新增方法
     * 有返回值
     * 实现Callable接口，重写call方法
     * 通过 FutureTask<Integer> futureTask = new FutureTask<>(new T3());
     *     new Thread(futureTask).start(); 启动
     * 通过futureTask.get();获取返回值
     */
    public static class T3 implements Callable{
        @Override
        public Object call() throws Exception {
            System.out.println("方式三：实现Callable创建线程");
            int sum = 0;
            for(int i=0;i<10;i++){
                sum = sum + i;
            }
            return sum;
        }
    }

    /**
     * 使用线程池
     * 提前创建好多个线程，放入线程池中，使用时直接获取，使用完放回池中。可以避免频繁创建销毁、实现重复利用
     * 优点：提高响应速度（减少了创建新线程的时间）
     *      降低资源消耗（重复利用线程池中线程，不需要每次都创建）
     *      便于线程管理
     * Executor 负责线程使用和调度的接口
     *  |-- ExecutorService接口 继承 Executor，线程池的主要接口
     *      |-- ThreadPoolExecutor 线程池的实现类，Executors.newFixedThreadPool(10);返回
     *     通过ThreadPoolExecutor的execute方法启动线程
     *
     *线程池相关概念:
     *  1-核心线程：若线程池中的线程标记为核心线程，即使核心线程没有运行任务，它也不会被销毁，会一直存在于线程池中，直至线程池被shutdown。
     *  2-非核心线程：当线程池中没有空闲的核心线程时，线程池会创建一个非核心线程，并且非核心线程的一定时间内处于空闲状态的时候，非核心线程会被销毁。
     *  3-阻塞队列：阻塞队列是当线程池中的没有能用于处理任务的线程时，会把该任务放入阻塞队列，待有能用于处理的线程时，把任务从队列取出处理，阻塞队列的长度可以设置。
     *  4-拒绝服务处理：当线程池中的没有线程能提供处理，并且阻塞队列的空间已满，此时会触发拒绝服务异常，开发人员可以根据自己的需求定制不同的处理策略。
     *
     *创建线程池的7个参数(一般我们推荐使用ThreadPoolExecutor（）自定义创建线程池，因为这比较灵活切可控)
     *  1-int corePoolSize  核心线程数，即确定有多少个核心线程。
     *  2-int maximumPoolSize  最大线程数，即限定线程池中的最大线程数量。
     *  3-long keepAliveTime  非核心线程的存活时间，配合下面的TimeUnit参数确定时间。
     *  4-TimeUnit unit  一个时间类型的枚举类。有从纳秒到天的时间量度，配合上面的keepAliveTime确定非核心线程的存活时间。
     *  5-BlockingQueue<Runnable> workQueue   装载Runnable的阻塞队列，具体类型可以自己确定。
     *  6-ThreadFactory threadFactory  线程工厂，这是一个函数式接口，里面只定义了一个newThread（Runnable task）方法，需要自己实现工厂的方法，在这里我们可以对线程进行自定义的初始化，例如给线程设定名字，这样方便后期的调试。
     *  7-RejectedExecutionHandler handler   拒绝服务处理，这也是一个函数式接口，我们需要实现rejectedExecution(Runnable r, ThreadPoolExecutor executor)这个方法，这里可以根据需求自定义你希望在处理逻辑。当然Java里面也有已经定义好的四种策略静态类。可以通过ThreadPoolExecutor调用
     *  ps：jdk提供了默认的工厂方法和默认的默认的拒绝处理策略。默认拒绝策略是：不执行并抛出异常
     *
     *Executors中实现的线程池类型
     *  1-FixedThreadPool       固定核心线程的线程池
     *      它的核心线程数量就是最大线程数，所以线程池内的线程永远不会消亡，它采用了无参数的链表阻塞队列，最大的任务数可达232-1个。因此存在任务积压导致内存溢出的风险
     *  2-CachedThreadPool   缓存线程池
     *      没有核心线程，线程池不能满足任务运行时会创建新的线程，线程数量没有上限。默认的消亡时间为60秒。值得注意的是：它的阻塞队列是SynchronousQueue，这是一个没有存储性质的阻塞队列，它的取值操作和放入操作必须是互斥的。根据源码文档的解释，可以理解为每当有任务放入时会立即有线程将它取出执行。
     *  3-ScheduledThreadPool  固定调度线程池
     *      有固定的核心线程，线程的数量没有限制，默认存活时间为60秒。同时支持定时及周期性任务执行。
     *  4-SingleThreadExecutor  单核心线程池
     *      只有一个核心线程，所以能保证任务的串行化执行。
     *  5-WorkStealingPool  并行执行线程池
     *      在jdk8中实现 线程池。它内部的线程池实现是ForkJoinPool，这是一个可以同时利用多个线程来执行任务的线程池。无参默认使用CPU数量的线程数执行任务
     *
     *线程池的关闭
     *  线程池可通过调用线程池的shutdown或shutdownNow方法来关闭线程池.
     *  shutdownNow首先将线程池的状态设置成STOP,然后尝试停止所有的正在执行或暂停任务的线程,并返回等待执行任务的列表
     *  shutdown只是将线程池的状态设置成SHUTDOWN状态，然后中断所有没有正在执行任务的线程.
     */
    public static class T4 implements Runnable{
        @Override
        public void run() {
            System.out.println("方式四：使用线程池启动线程");
            for(int i=0;i<10;i++){
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        new T1().start();

        new Thread(new T1()).start();
        new Thread(new T2()).start();

        FutureTask<Integer> futureTask = new FutureTask<>(new T3());
        new Thread(futureTask).start();
        Integer sum = futureTask.get();
        System.out.println("返回结果：sum = " + sum);

        ExecutorService pool = Executors.newFixedThreadPool(3);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) pool;
        executor.execute(new T4());
        executor.execute(new T4());
        executor.execute(new T4());
        executor.execute(new T4());
        executor.execute(new T4());
        executor.shutdown();

        Thread t5 = new Thread(()->{
            System.out.println("使用Lamaba方式创建线程可以算是创建线程的第五种方式");
        });
        t5.start();
    }
}
