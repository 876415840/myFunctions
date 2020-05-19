package com.alan.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: 手写一个LRU（容器先进先出，可以倒叙取先进后出）
 * @Author MengQingHao
 * @Date 2020/5/19 3:34 下午
 */
public class UseLinkedHashMapLRU<K, V> extends LinkedHashMap<K, V> {

    /**
     * 指定数量大小
     */
    private int size;

    public UseLinkedHashMapLRU(int size) {
        // 按照插入顺序
        // super();
        // 按照访问顺序
        super(16, 0.75f, true);
        this.size = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest){
        // 超过指定大小，删除链表顶端(先进)数据
        return size()>size;
    }

}
