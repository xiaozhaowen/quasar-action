package com.xiaozhao.quasar.okhttp;

import co.paralleluniverse.fibers.*;
import co.paralleluniverse.fibers.okhttp.FiberOkHttpClient;
import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author xiaozhao
 * @date 2019-07-2416:25
 */
@Slf4j
public class GetExample {

    OkHttpClient client = new FiberOkHttpClient();

    private FiberScheduler scheduler;

    public GetExample() {
        scheduler = new FiberForkJoinScheduler("test", 4, null, false);
    }

    static abstract class HttpFiberAysnc extends FiberAsync<String, RuntimeException>
            implements Callback {
        @Override
        public void onFailure(Request request, IOException e) {
            log.info("产生异常");
            asyncFailed(e);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            log.info("正常返回");
            asyncCompleted(response.body().string());
        }

        //        @Override
//        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            log.info("产生异常");
//            asyncFailed(e);
//        }
//
//        @Override
//        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//            log.info("正常返回");
//            asyncCompleted(response.body().string());
//        }
    }

    private String requestWeb(String url) throws InterruptedException, SuspendExecution {
        return new HttpFiberAysnc() {
            @Override
            protected void requestAsync() {
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                Call call = client.newCall(request);
                call.enqueue(this);
            }
        }.run();
    }

    private void testAsyncCallback(String url) {
        log.info("主线程开始");
        Fiber<String> fiber = new Fiber<>(scheduler, () -> {
            String result = requestWeb(url);
            return result;
        });

        log.info("主线程步骤2");

        try {
            String result = fiber.get();
            log.info("拿到结果");
            log.info(result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("主线程结束");

    }


    public static void main(String[] args) throws IOException {
        GetExample example = new GetExample();
        String url = "https://www.bennyhuo.com/";
        example.testAsyncCallback(url);
    }


}
