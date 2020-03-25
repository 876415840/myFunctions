package com.alan.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.alan.service.SpidersService

/**
 * @Description 爬虫
 * @Author mengqinghao
 * @Date 2018/11/7 11:23 AM
 * @Version 1.0
 */
@RestController
@RequestMapping("/spiders")
class SpidersController {

    private val LOGGER = LoggerFactory.getLogger(SpidersController::class.java)

    @Autowired
    @Qualifier("realtySpidersService")
    lateinit var realtySpidersService: SpidersService

    @GetMapping("/realtyInfo")
    fun realtyInfo(): String {
        var str: String = realtySpidersService!!.spidersInfo()
        return "爬虫$str"
    }
}