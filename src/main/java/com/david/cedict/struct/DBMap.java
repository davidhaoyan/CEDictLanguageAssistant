package com.david.cedict.struct;

import com.david.cedict.util.MapVisitor;

import java.util.Set;

public interface DBMap<K, V> {
    public void put(K key, V value);
    public V get(K key);
    public DBSerial serialize(MapVisitor mv);
    public Set<K> keySet();
    public int size();
    public void print();
}
