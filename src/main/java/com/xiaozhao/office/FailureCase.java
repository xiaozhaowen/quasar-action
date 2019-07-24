package com.xiaozhao.office;

import co.paralleluniverse.fibers.SuspendExecution;

/**
 * 错误的实例
 * 1）构造函数不能声明为挂起函数
 * 2）类初始化器 也不能
 * 3）synchronized 修饰的方法或者 包含 synchronized 代码块的不能声明为挂起方法；如非要声明为挂起，请指定JavaAgent 的m 参数或AntTask 的allowMonitors
 * 4）调用了线程的阻塞API不能声明为挂起方法；如非要声明为挂起，请指定JavaAgent 的b 参数或AntTask 的 allowBlocking
 *
 * @author xiaozhao
 * @date 2019-07-2408:57
 */
public class FailureCase {

    public static void main(String[] args) {
        FailureCase failureCase = new FailureCase();
        try {
            failureCase.synchronizedBlockFail();
        } catch (SuspendExecution suspendExecution) {
            suspendExecution.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造函数不能声明为挂起函数
     *
     * @throws SuspendExecution
     */
    public FailureCase()
//            throws SuspendExecution
    {
    }

    /**
     * synchronized 修饰的方法不能声明为挂起
     */
    private synchronized void synchronizedMethodFail()
//            throws SuspendExecution,InterruptedException
    {
        System.out.println("synchronized method  fail");
    }

    /**
     * TODO 方法内部包含了 synchronized 代码块，但是没有报错
     *
     * @throws SuspendExecution
     * @throws InterruptedException
     */
    private void synchronizedBlockFail()
            throws SuspendExecution, InterruptedException {

        synchronized (FailureCase.class) {
            System.out.println("synchronized block fail");
        }

        System.out.println("synchronized block fail");
    }

    /**
     * 调用了线程阻塞API的方法不能声明为挂起
     *
     * @throws InterruptedException
     */
    private void threadBlockingMethod()
            throws InterruptedException
//            , SuspendExecution
    {
        Thread.sleep(1000);
        System.out.println("Thread blocking  fail");
    }


}
