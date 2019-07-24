package com.xiaozhao.quasar.okhttp;

import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author xiaozhao
 * @date 2019-07-2417:23
 */
@Slf4j
public class RealHttp {


    OkHttpClient client = new OkHttpClient();


    String run(String url) throws IOException {
        log.info("开始");
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                log.info("请求出错:{}", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                log.info("请求返回");
                log.info(response.body().string());
            }

//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//
//            }
        });

        log.info("方法返回了");
        return "OK";
    }

    public static void main(String[] args) throws IOException {
        RealHttp realHttp = new RealHttp();
        String url = "https://www.bennyhuo.com/";
        realHttp.run(url);
    }
}
