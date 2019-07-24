package com.xiaozhao.quasar.example.fibers;

import co.paralleluniverse.fibers.*;
import co.paralleluniverse.strands.SuspendableRunnable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaozhao
 * @date 2019-07-2415:19
 */
@Slf4j
public class FiberAsyncTest {
    private FiberScheduler scheduler;

    public FiberAsyncTest() {
        scheduler = new FiberForkJoinScheduler("test", 4, null, false);
    }

    interface MyCallback {
        void call(String str);

        void fail(RuntimeException e);
    }

    interface Service {
        void registerCallback(MyCallback callback);
    }

    final ExecutorService executor = Executors.newFixedThreadPool(1);

    final Service asyncService = new Service() {
        @Override
        public void registerCallback(final MyCallback callback) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        callback.call("async result!");
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

        }
    };

    /**
     * 这是一个挂起函数
     *
     * @param service
     * @return
     * @throws SuspendExecution
     * @throws InterruptedException
     */
    String callService(final Service service) throws SuspendExecution, InterruptedException {
        return new MyFiberAsync() {
            @Override
            protected void requestAsync() {
                service.registerCallback(this);
            }
        }.run();
    }

    /**
     * 启动一个协程
     */
    private void testAsyncCallback() {
        Fiber fiber = new Fiber(this.scheduler, new SuspendableRunnable() {
            @Override
            public void run() throws SuspendExecution, InterruptedException {
                String result = callService(asyncService);
                log.info("返回结果：{}", result);
            }
        }).start();

        try {
            fiber.join();
            executor.shutdown();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static abstract class MyFiberAsync extends FiberAsync<String, RuntimeException>
            implements MyCallback {

        private final Fiber fiber;

        public MyFiberAsync() {
            this.fiber = Fiber.currentFiber();
        }

        @Override
        public void call(String str) {
            super.asyncCompleted(str);
        }

        @Override
        public void fail(RuntimeException e) {
            super.asyncFailed(e);
        }
    }


    public static void main(String[] args) {
        log.info("主线程开始");
        FiberAsyncTest test = new FiberAsyncTest();
        test.testAsyncCallback();
        log.info("主线程结束");
    }
}
