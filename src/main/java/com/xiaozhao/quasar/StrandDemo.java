package com.xiaozhao.quasar;

/**
 * Strand 是 线程与协程的抽象
 * LockSupport 与 Strand.park() Strand.unpark() 方法一般是用不到的
 *
 * park()方法应该被Strand自己调用，并且只可以被其他的Strand 调用unpark()
 * @author xiaozhao
 * @date 2019-07-2409:51
 */
public class StrandDemo {
    
}
