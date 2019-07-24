package com.xiaozhao.office;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.SuspendableCallable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 在同一个协程内部，挂起方法按照同步方法先后顺序执行
 * <p>
 * 2个不同的协程如果没有依赖关系，则会并行执行
 *
 * @author xiaozhao
 * @date 2019-07-2410:14
 */
@Slf4j
public class ExecOrder {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FiberExecutorScheduler scheduler = new FiberExecutorScheduler("myPool", executor);


        Fiber<String> fiber = new Fiber<>(scheduler, (SuspendableCallable<String>) () -> {
            log.info("协程开始");
            ExecOrder execOrder = new ExecOrder();
            String name = execOrder.getName(24);
            log.info(name);
            String addr = execOrder.getAddress();
            log.info(addr);
            log.info("协程结束");
            return addr;
        }).start();

        Fiber<String> fiber2 = new Fiber<>(scheduler, (SuspendableCallable<String>) () -> {
            log.info("协程2开始");
            ExecOrder execOrder = new ExecOrder();
            String male = execOrder.getMale();
            log.info(male);
            log.info("协程2结束");
            return male;
        }).start();

        try {
            fiber.join();
            fiber2.join();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduler.shutdown();
        executor.shutdownNow();
        log.info("主线程结束");
    }

    private String getMale() throws SuspendExecution, InterruptedException {
        Strand.sleep(1000);
        return " Male";
    }

    private String getAddress() throws SuspendExecution, InterruptedException {
        Strand.sleep(2000);
        return " Los Angeles";
    }

    private String getName(int id) throws SuspendExecution, InterruptedException {
        Strand.sleep(3000);
        return "Kobe Bryant " + id;
    }
}
