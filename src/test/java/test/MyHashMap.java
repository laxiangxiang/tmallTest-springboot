package test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LXX on 2019/2/20.
 */
public class MyHashMap<K,V> {
    private Entry<K,V>[] table;
    private static final Integer CAPACITY = 2;
    private int size;

    public void put(K k,V v){
        int index = hash(k);
        if (table == null){
            inflate();
        }
//        table[index] = new Entry<>(k,v);
            for (Entry<K,V> entry = table[index];entry!=null;entry = entry.next){
                if (k.equals(entry.getKey())){
                    entry.value = v;
                    return;
                }
            }

        addEntry(k,v,index);
    }

    private void addEntry(K k, V v, int index) {
        Entry<K,V> newEntry = new Entry<K, V>(k,v,table[index]);
        table[index] = newEntry;
        size ++;
    }

    private void inflate() {
        table = new Entry[CAPACITY];
    }

    public V get(K k){
        int index = hash(k);
//        return table[index].getValue();
        V value = null;
        for (Entry<K,V> entry = table[index];entry!=null;entry = entry.next){
            if (k.equals(entry.getKey())){
                value =  entry.getValue();
                break;
            }
        }
        return value;
    }

    private int hash(K k){
        //保证计算出的hash值在table的大小之内
//        return k.hashCode() % table.length;
        //源码中key的hash算法
        int h;
        return (k == null) ? 0 : (h = k.hashCode()) ^ (h >>> 16);
    }
    public MyHashMap() {
        this.table = new Entry[CAPACITY];
    }

    static class Entry<K,V>{
        private K key;
        private V value;
        private Entry<K,V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    public static void main(String[] args){
        MyHashMap<String,String> myHashMap = new MyHashMap<>();
        myHashMap.put("3","3");
        myHashMap.put("2","2");
        myHashMap.put("1","1");
        System.out.println(myHashMap.get("1"));

        HashMap<Integer,Integer> hashMap = new HashMap<Integer, Integer>(){
            {
                put(1,1);
                put(2,2);
                put(3,3);
            }
        };

        Set<Integer> keySet = hashMap.keySet();
        //fail-fast: ConcurrentModificationException
        //如果映射在迭代器创建之后的任何时间被结构地修改，迭代器将抛出此异常
        //可以使用ConcurrentHashMap或者通过迭代器自己的remove方法
        for (Integer key : keySet){
            if (2==key){
                hashMap.put(4,4);
//                hashMap.remove(2);
            }
        }

        Iterator<Integer> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()){
            if (2== iterator.next()){
                iterator.remove();
            }
        }
        Iterator<Map.Entry<Integer,Integer>> iterator1 = hashMap.entrySet().iterator();
        while (iterator1.hasNext()){
            if (3 == iterator1.next().getKey()){
                iterator1.remove();
            }
        }
    }
}
