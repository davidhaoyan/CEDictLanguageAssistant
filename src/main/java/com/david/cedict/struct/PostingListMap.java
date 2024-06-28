package com.david.cedict.struct;

import com.david.cedict.util.MapVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PostingListMap implements DBMap<Integer,Integer> {
    private Map<Integer,Integer> map = new HashMap<>();

    public PostingListMap() {}
    public PostingListMap(Map<Integer,Integer> init) {
        for (Integer key : init.keySet()) {
            map.put(key, init.get(key));
        }
    }
    public void put(Integer key, Integer value) {
        map.put(key, value);
    }
    public Integer get(Integer key) {
        return map.get(key);
    }

    public PostingListSerial serialize(MapVisitor mv) {
        return mv.visit(this);
    }

    public Set<Integer> keySet() {
        return map.keySet();
    }

    public int size() {
        return map.size();
    }

    public void print() {
        for (Integer key : map.keySet()) {
            System.out.println(key + ":" + map.get(key));
        }
    }
}
