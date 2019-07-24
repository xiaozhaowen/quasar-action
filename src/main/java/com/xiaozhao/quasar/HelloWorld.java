package com.xiaozhao.quasar;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableCallable;
import co.paralleluniverse.strands.SuspendableRunnable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * Fiber 这个类除了构造函数，start，join，get之外不应该再访问这个类的其他方法。
 * Fiber在模拟线程，如果需要操作类似线程的一些行为，请使用Strand 这个类，这个类是对线程和协程的抽象
 *
 * 有3种方式：
 * 1）Fiber中覆盖run方法
 * 2）Fiber构造函数中传递 SuspendableCallable
 * 3）Fiber构造函数中传递 SuspendableRunnable
 *
 * @author xiaozhao
 * @date 2019-07-2315:03
 */
@Slf4j
public class HelloWorld {
    public static void main(String[] args) {
        fiberStart3();
    }

    private static void fiberStart1() {
        /**
         * 启动一个协程
         */
        Fiber fiber = new Fiber<String>() {
            @Override
            protected String run() throws SuspendExecution, InterruptedException {
                // your code
                System.out.println("Inner Fiber");
                return "OK";
            }
        }.start();

        String result = null;
        try {
            /**
             * get 方法会join协程并且拿到返回值
             */
            result = (String) fiber.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        System.out.println("Hello Quasar");
    }

    private static void fiberStart2() {
        new Fiber(new SuspendableCallable() {
            @Override
            public Object run() throws SuspendExecution, InterruptedException {
                log.info("SuspendableCallable Fiber");
                return "Hello";
            }
        }).start();

        log.info("外部执行");
    }

    private static void fiberStart3() {
        new Fiber(new SuspendableRunnable() {
            @Override
            public void run() throws SuspendExecution, InterruptedException {
                log.info("SuspendableRunnable Fiber");
            }
        }).start();

        log.info("外部执行");
    }
}

