package com.xiaozhao.quasar

import co.paralleluniverse.fibers.Suspendable
import co.paralleluniverse.kotlin.fiber

/**
 * 在kotlin中使用Quasar协程
 */
fun main() {
    /**
     * 创建并且启动一个协程
     */
    val fiber = fiber @Suspendable {
        println("Kotlin  协程")
        "Hello Name"
    }

    val result = fiber.get()
    println("协程返回结果： $result")
    println("主函数结束")
}