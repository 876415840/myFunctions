package com.alan.controller;

import com.alan.mapper.UserMapper;
import com.alan.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午3:13
 * @Modified By:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/add")
    public String add(@RequestBody User user){
        int result = userMapper.insert(user);
        logger.info("我是info");
        logger.trace("我是trace");
        logger.debug("我是debug");
        logger.warn("我是warn");
        logger.error("我是error");
        return "影响行数"+result;
    }
}
