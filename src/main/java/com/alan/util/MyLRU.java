package com.alan.util;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 手写实现LRU
 * @Author MengQingHao
 * @Date 2020/3/24 6:02 下午
 * @Version 1.3.0
 */
public class MyLRU<K, V> implements Serializable {

    private static final long serialVersionUID = 4896373822600178082L;

    /**
     * 容器对象数量
     */
    private transient volatile int size = 0;
    /**
     * 容器大小
     */
    private transient volatile int initialCapacity = 10;
    /**
     * 容器map
     */
    private transient volatile Map<K, Node<K,V>> map = new ConcurrentHashMap();
    /**
     * 首对象
     */
    private transient volatile Node<K,V> head;
    /**
     * 尾对象
     */
    private transient volatile Node<K,V> tail;
    /**
     * 下个对象
     */
    private transient volatile Node<K,V> next;

    public MyLRU(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public MyLRU() {
    }

    static class Node<K,V> {
        public K k;
        public V v;
        public Node<K,V> next;
        public Node<K,V> pre;
        public Node(K k, V v, Node<K,V> next){
            this.k = k;
            this.v = v;
            this.next = next;
            if (next != null) {
                this.next.pre = this;
            }
        }
    }

    public void add(K key, V value) {
        if (empty()) {
            Node<K,V> c = new Node<K,V>(key, value, null);
            this.head = c;
            this.tail = c;
            this.next = c;
            map.put(key, c);
            size++;
            return;
        }

        Node<K,V> old = map.get(key);
        if (old != null) {
            delete(old);
        }

        if (this.size == this.initialCapacity) {
            Node<K,V> tail = this.tail;
            tail.pre.next = null;
            this.tail = tail.pre;
            delete(tail);
        }

        Node<K,V> c = new Node<K,V>(key, value, this.head);
        this.head = c;
        this.next = c;
        map.put(key, c);
        size++;
    }

    public V next() {
        Node<K,V> node = this.next;
        if (node == null) {
            return null;
        }
        this.next = node.next;
        return node.v;
    }

    public boolean hasNext() {
        return this.next != null;
    }
    
    private void delete (Node<K,V> node) {
        this.map.remove(node.k);
        Node<K,V> next = node.next;
        Node<K,V> pre = node.pre;
        if (pre == null) {
            next.pre = null;
            this.head = next;
        } else if (next == null) {
            pre.next = null;
            this.tail = pre;
        } else {
            next.pre = pre;
            pre.next = next;
        }
        size--;
    }

    public boolean empty() {
        return this.size == 0;
    }

}
