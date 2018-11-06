package com.alan.enums;

/**
 * @author mengqinghao
 * @Title: myFunctions
 * @Package com.alan.enums
 * @Description: ${TODO}
 * @date 2018/4/8下午3:27
 */
public enum MyEnum {
    A("a"),B("b"),C("c");
    private String value;

    private MyEnum(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value ;
    }
}
