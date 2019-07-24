package com.xiaozhao.quasar;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.SuspendableCallable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * 挂起方法：凡是声明抛出SuspendExecution异常的方法，被称为挂起方法
 * 挂起方法需要抛出SuspendExecution 异常
 * <p>
 * 如果一个方法A是挂起方法，如果方法B要调用方法A，那么方法B也需要声明为挂起方法
 * <p>
 * 挂起方法有2种形式：
 * 1）抛出 SuspendExecution异常，这种是推荐的形式，因为编译器防止你忘记抛出 SuspendExecution
 * 2）使用@Suspendbale注解。这种形式适用于方法已经抛出了其他异常或者继承了接口，接口方法本身有异常抛出。所以需要使用注解的形式
 *
 * @author xiaozhao
 * @date 2019-07-2317:17
 */
@Slf4j
public class SuspendMethod {
    public static void main(String[] args) {
        log.info("主线程开始1");

        Fiber<String> fiber = new Fiber<String>(new SuspendableCallable<String>() {
            @Override
            public String run() throws SuspendExecution, InterruptedException {
                SuspendMethod suspendMethod = new SuspendMethod();
                String addr = suspendMethod.getAddress();
                return addr;
            }
        }).start();

        log.info("主线程开始2");

        try {
            String addr = fiber.get();
            log.info("获取到值：{}", addr);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("主线程结束");

    }


    private String getAddress() throws SuspendExecution, InterruptedException {
        String name = getName(24);
        Strand.sleep(2000);
        return name + "  is in NewYork";
    }

    private String getName(int id) throws SuspendExecution, InterruptedException {
        Strand.sleep(3000);
        return "Kobe Bryant " + id;
    }
}
