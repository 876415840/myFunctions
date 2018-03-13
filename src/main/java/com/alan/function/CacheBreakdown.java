package com.alan.function;

import com.alan.mapper.UserMapper;
import com.alan.model.User;
import com.alan.service.CacheService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * 缓存击穿
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午2:37
 * @Modified By:
 */
@Component
public class CacheBreakdown {

    private Logger logger = Logger.getLogger(CacheBreakdown.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private CacheService cacheService;

    private CountDownLatch cdl = new CountDownLatch(200);

    public void crashTest(){
         for(int i=0; i < 200; i++){
             new Thread(new UserThread()).start();
             //倒计时器倒计一次
             cdl.countDown();
         }
    }

    class UserThread implements Runnable{

        @Override
        public void run() {
            try {
                //已实例化好的线程在此等待，当所有线程实例化完成后，同时停止等待
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String name  = getNameByCache("1");
            logger.info(Thread.currentThread().getName()+"=========="+name);
        }

        /**
         * 从缓存中取用户名
         * @param userId
         * @return
         */
        public String getNameByCache(String userId) {
            //1.从缓存里取数据
            User user = cacheService.cacheResult(userId, "user");
            //2.如果缓存中有，则取出
            if(user != null){
                logger.info("==============================get data from cache=============================");
                return user.getName();
            }
            //3.如果缓存中没有，则从数据库中取
            user = userMapper.findUserById(Integer.parseInt(userId));
            //4.将数据库中取出的数据，放到缓存
            if(user != null)
                cacheService.cachePut(userId,user,"user");
            return user.getName();
        }
    }

}
