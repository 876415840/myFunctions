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
        map.put(key, c);
        size++;
    }

    public V get(K key) {
        Node<K,V> node = map.get(key);
        if (node == null) {
            return null;
        }
        Node<K,V> next = node.next;
        Node<K,V> pre = node.pre;
        if (pre == null) {
            return node.v;
        }
        if (next == null) {
            pre.next = null;
            this.tail = pre;
        } else {
            next.pre = pre;
            pre.next = next;
        }
        node.next = this.head;
        this.head.pre = node;
        this.head = node;
        return node.v;
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

    public static void main(String[] args) {
        MyLRU<String, Integer> myLRU = new MyLRU<String, Integer>(3);
        myLRU.add("key1", 7);
        print(myLRU);
        myLRU.add("key2", 0);
        print(myLRU);
        myLRU.add("key3", 1);
        print(myLRU);
        myLRU.add("key4", 2);
        print(myLRU);
        myLRU.get("key2");
        print(myLRU);
        myLRU.add("key5", 3);
        print(myLRU);
        myLRU.get("key2");
        print(myLRU);
        myLRU.add("key6", 4);
        print(myLRU);

    }

    private static void print(MyLRU<String, Integer> myLRU) {
        Node<String,Integer> node = myLRU.head;
        while (node != null) {
            System.out.println(node.v);
            node = node.next;
        }
        System.out.println("------------------------------------");
    }
}
