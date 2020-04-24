package com.alan.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 线程池
 * @Author MengQingHao
 * @Date 2020/3/27 12:01 下午
 * @Version 1.3.0
 */
public class MyThreadPoolExecutor {
    public static final Logger LOGGER = LoggerFactory.getLogger(MyThreadPoolExecutor.class);

    private volatile int index = 0;

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 2000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 30; i++) {
            threadPoolExecutor.execute(new AThread(i));
        }
    }

    public void add() {
        synchronized (this) {
            this.index++;
            LOGGER.info("--add--->{}", index);
        }
    }

    static class AThread implements Runnable {

        public static final Logger LOGGER = LoggerFactory.getLogger(MyThreadPoolExecutor.AThread.class);


        private int index;

        public AThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("{}<--=-->{}", index, Thread.currentThread().getId());
        }
    }
}
