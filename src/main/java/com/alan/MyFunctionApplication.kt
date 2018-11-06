package com.alan

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午5:42
 * @Modified By:
 */
@SpringBootApplication
object MyFunctionApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(MyFunctionApplication::class.java, *args)
    }
}
