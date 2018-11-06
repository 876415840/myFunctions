package com.alan.controller

import com.alan.mapper.UserMapper
import com.alan.model.User
import com.alan.service.CacheService
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.annotation.Resource
import java.util.HashMap
import java.util.concurrent.CountDownLatch

/**
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午3:13
 * @Modified By:
 */
@RestController
@RequestMapping("/user")
class UserController {

    private val logger = Logger.getLogger(UserController::class.java)


    @Resource
    private val cacheService: CacheService? = null

    private val cdl = CountDownLatch(100000)

    @Autowired
    //private UserMapper userMapper;

    @RequestMapping("/add")
    fun add(): String {

        return "影响行数"
    }

    fun crashTest() {
        val map = HashMap<String, Int>()
        for (i in 20000..129999) {
            map["num"] = i
            Thread(UserThread(map)).start()
            //倒计时器倒计一次
            cdl.countDown()
        }
    }

    internal inner class UserThread(map: Map<String, Int>) : Runnable {

        var num: Int? = 1

        init {
            this.num = map["num"]
        }

        override fun run() {
            try {
                //已实例化好的线程在此等待，当所有线程实例化完成后，同时停止等待
                cdl.await()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val name = getNameByCache(num.toString() + "")
            logger.info(Thread.currentThread().name + "=====" + num + "=====" + name)
        }

        /**
         * 从缓存中取用户名
         * @param userId
         * @return
         */
        fun getNameByCache(userId: String): String {
            //            //1.从缓存里取数据
            //            User user = cacheService.cacheResult(userId, "user");
            //            //2.如果缓存中有，则取出
            //            if(user != null){
            //                logger.info("==============================get data from cache=============================");
            //                return user.getName();
            //            }
            //            //3.如果缓存中没有，则从数据库中取
            //            user = userMapper.findUserById(Integer.parseInt(userId));
            //            logger.info("==============================get data from mysql=============================");
            //            //4.将数据库中取出的数据，放到缓存
            //            if(user != null)
            //                cacheService.cachePut(userId,user,"user");
            //            return user.getName();
            return ""
        }
    }
}
