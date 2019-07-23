package com.xiaozhao.office;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;

import java.util.concurrent.ExecutionException;

/**
 * Fiber 这个类除了构造函数，start，join，get之外不应该再访问这个类的其他方法。
 * Fiber在模拟线程，如果需要操作类似线程的一些行为，请使用Strand 这个类，这个类是对线程和协程的抽象
 *
 * @author xiaozhao
 * @date 2019-07-2315:03
 */
public class HelloWorld {
    public static void main(String[] args) {

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
}
