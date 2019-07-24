package com.xiaozhao.office;

import co.paralleluniverse.fibers.FiberAsync;
import co.paralleluniverse.fibers.SuspendExecution;

/**
 * 包装异步
 * 异步调用转同步挂起
 * <p>
 * String 为 异步请求返回的值类型
 *
 * @author xiaozhao
 * @date 2019-07-2409:58
 */
public class AsyncFiberDemo {
    public static void main(String[] args) {

    }


    /**
     * TODO 方法调用示例代码不全
     *
     * @return
     */
    String op() {
        try {
            /**
             * run方法会挂起，直到异步结果返回了
             */
            new FooAsync() {
                @Override
                protected void requestAsync() {
                    // 调用异步方法
                    //                Foo.asyncOp(this);
                }
            }.run();
        } catch (SuspendExecution suspendExecution) {
            suspendExecution.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}

abstract class FooAsync extends FiberAsync<String, FooException> implements FooCompletion {


    @Override
    public void success(String result) {
        asyncCompleted(result);
    }

    @Override
    public void failure(FooException exception) {
        asyncFailed(exception);
    }
}


/**
 * 回调接口
 */
interface FooCompletion {
    void success(String result);

    void failure(FooException exception);
}

/**
 * 自定义异常
 */
class FooException extends RuntimeException {

}
