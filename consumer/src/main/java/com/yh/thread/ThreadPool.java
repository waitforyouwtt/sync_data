package com.yh.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    public static void main(String[] args) {

        /**
         * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
         */
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i= 0;i < 10;i ++){
            final int index  = i;
            try{
                Thread.sleep(index * 1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(index);
                }
            });
        }

        /**
         * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
         */
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable(){
                @Override
                public void run() {
                    try {
                        System.out.println("打印当前index："+index);
                        Thread.sleep(5000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        /**
         * 创建一个周期性线程池，支持定时及周期性任务执行。延迟执行示例
         */
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟3秒钟");
            }
        },5, TimeUnit.SECONDS);

        /**
         * 创建一个周期性线程池，支持定时及周期性任务执行。定期执行示例
         */
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("delay 1 seconds, and excute every 3 seconds");
                System.out.println("延迟1秒执行，每隔3秒钟执行一次");
            }
        },1,3,TimeUnit.SECONDS);

        /**
         * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
         */
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("打印当前的值："+index);
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            final int index = i;
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        System.out.println("打印当前index:"+index);
                        try {
                            Thread.sleep(1000*10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}