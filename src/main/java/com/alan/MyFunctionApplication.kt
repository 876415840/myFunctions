package com.alan

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午5:42
 * @Modified By:
 */
@SpringBootApplication
open class MyFunctionApplication {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(MyFunctionApplication::class.java)
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(MyFunctionApplication::class.java, *args)
            LOGGER.info("Boot Server started.")
        }
    }
}
