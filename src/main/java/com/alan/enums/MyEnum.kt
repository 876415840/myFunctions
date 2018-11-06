package com.alan.enums

/**
 * @author mengqinghao
 * @Title: myFunctions
 * @Package com.alan.enums
 * @Description: ${TODO}
 * @date 2018/4/8下午3:27
 */
enum class MyEnum private constructor(private val value: String) {
    A("a"), B("b"), C("c");

    override fun toString(): String {
        return this.value
    }
}
