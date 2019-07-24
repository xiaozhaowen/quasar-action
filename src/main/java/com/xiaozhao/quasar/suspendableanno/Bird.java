package com.xiaozhao.quasar.suspendableanno;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.Strand;

/**
 * Fly接口的实现类
 *
 * @author xiaozhao
 * @date 2019-07-2411:00
 */
public class Bird implements Fly {


    /**
     * 因为实现了Fly接口，所以不能抛出 SuspendExecution 异常，否则编译失败
     *
     * @param name
     * @return
     */
    @Suspendable
    @Override
    public String fly(String name) {
        try {
            String birdType = suspendMethod();
        }
        // 织入代码会处理掉这个catch代码块，不会让这个catch块执行
        catch (SuspendExecution suspendExecution) {
            throw new AssertionError(suspendExecution);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return name + "用翅膀飞行";
    }

    /**
     * 我们想要在协程中运行这个方法
     * 这个方法添加了 @Suspendable注解，因为调用了挂起方法fly
     * TODO 这种形式的话需要指定 META-INF 的挂起方法 或者使用自动检测
     *
     * @param f
     */
    @Suspendable
    public void h(Fly f) {
        f.fly("麻雀");
    }


    private String suspendMethod() throws SuspendExecution, InterruptedException {
        Strand.sleep(2000);
        return "啄木鸟";
    }
}
