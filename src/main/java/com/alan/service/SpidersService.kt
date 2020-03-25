package com.alan.service

/**
 * @Description 爬虫接口
 * @Author mengqinghao
 * @Date 2018/11/7 11:11 AM
 * @Version 1.0
 */
interface SpidersService {

    /**
     * 爬去网页信息
     */
    fun spidersInfo(): String
}