package com.alan.service.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import com.alan.service.SpidersService

/**
 * @Description 房地产爬虫实现
 * @Author mengqinghao
 * @Date 2018/11/7 11:13 AM
 * @Version 1.0
 */
@Service("realtySpidersService")
class RealtySpidersServiceImp : SpidersService {

    private val LOGGER = LoggerFactory.getLogger(RealtySpidersServiceImp::class.java)

    /**
     * 爬去价格信息
     */
    override fun spidersInfo(): String {
        LOGGER.info("=====================>> 房地产爬虫")
        return "hello world"
    }
}