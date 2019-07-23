package com.xiaozhao.office;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 协程调度器
 * 可以在Fiber构造函数中指定调度器，如果不指定，则有一个默认的调度器。这个默认调度器使用的是ForkJoinPool 。
 * 如果想要指定自己的线程池或者限定在某个协程上，请使用 【FiberExecutorScheduler】
 *
 * @author xiaozhao
 * @date 2019-07-2315:43
 */
@Slf4j
public class SchedulerDemo {


    public static void main(String[] args) {

        /**
         * 指定我们自己的线程池
         */
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FiberExecutorScheduler scheduler = new FiberExecutorScheduler("myPool", executor);


        Fiber fiber = new Fiber<String>(scheduler) {
            @Override
            protected String run() throws SuspendExecution, InterruptedException {
                // your code
                log.info("AA");
                System.out.println("Inner Fiber  " + Thread.currentThread().getName());
                executor.shutdown();
                return "OK";
            }
        }.start();

    }


}
