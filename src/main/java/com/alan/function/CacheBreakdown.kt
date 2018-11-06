package com.alan.function

import com.alan.mapper.UserMapper
import com.alan.model.User
import com.alan.service.CacheService
import org.apache.log4j.Logger
import org.springframework.stereotype.Component

import javax.annotation.Resource
import java.util.concurrent.CountDownLatch

/**
 * 缓存击穿
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午2:37
 * @Modified By:
 */
@Component
class CacheBreakdown {

    private val logger = Logger.getLogger(CacheBreakdown::class.java)

    @Resource
    private val userMapper: UserMapper? = null

    @Resource
    private val cacheService: CacheService? = null

    private val cdl = CountDownLatch(200)

    fun crashTest() {
        for (i in 0..199) {
            Thread(UserThread()).start()
            //倒计时器倒计一次
            cdl.countDown()
        }
    }

    internal inner class UserThread : Runnable {

        override fun run() {
            try {
                //已实例化好的线程在此等待，当所有线程实例化完成后，同时停止等待
                cdl.await()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val name = getNameByCache("1")
            logger.info(Thread.currentThread().name + "==========" + name)
        }

        /**
         * 从缓存中取用户名
         * @param userId
         * @return
         */
        fun getNameByCache(userId: String): String? {
            //1.从缓存里取数据
            var user: User? = cacheService!!.cacheResult(userId, "user")
            //2.如果缓存中有，则取出
            if (user != null) {
                logger.info("==============================get data from cache=============================")
                return user!!.name
            }
            //3.如果缓存中没有，则从数据库中取
            user = userMapper!!.findUserById(Integer.parseInt(userId))
            //4.将数据库中取出的数据，放到缓存
            if (user != null)
                cacheService!!.cachePut(userId, user, "user")
            return user!!.name
        }
    }

}
