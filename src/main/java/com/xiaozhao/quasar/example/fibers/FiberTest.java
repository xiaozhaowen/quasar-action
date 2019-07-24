package com.xiaozhao.quasar.example.fibers;

import co.paralleluniverse.fibers.*;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaozhao
 * @date 2019-07-2414:38
 */
@Slf4j
public class FiberTest {

    FiberScheduler scheduler;


    public FiberTest() {
        scheduler = new FiberExecutorScheduler("test",
                Executors.newFixedThreadPool(
                        1,
                        new ThreadFactoryBuilder().setNameFormat("fiber-scheduler-%d").setDaemon(true).build()
                )
        );

//        scheduler =   new FiberForkJoinScheduler("test", 4, null, false);


    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        FiberTest test = new FiberTest();
        test.testTimeout();
    }

    /**
     * 超时测试，join有一个等待时间
     */
    private void testTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        log.info("Start");
        Fiber fiber = new Fiber(scheduler, (SuspendableRunnable) () -> {
            Fiber.park(2000, TimeUnit.MILLISECONDS);
        }).start();

        try {
            fiber.join(1000, TimeUnit.MILLISECONDS);
            log.info("Join OK");
        } catch (java.util.concurrent.TimeoutException e) {
            log.info("Join fail");
        }

        fiber.join(3000, TimeUnit.MILLISECONDS);
        log.info("Join OK");
    }

}
