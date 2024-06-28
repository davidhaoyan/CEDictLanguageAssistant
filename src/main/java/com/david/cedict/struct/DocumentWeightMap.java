package com.david.cedict.struct;

import com.david.cedict.util.MapVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentWeightMap implements DBMap<String, Float> {
    private Map<String,Float> map = new HashMap<>();

    public DocumentWeightMap() {}
    public DocumentWeightMap(Map<String,Float> init) {
        for (String key : init.keySet()) {
            map.put(key, init.get(key));
        }
    }

    public void put(String key, Float value) {
        map.put(key, value);
    }

    public Float get(String key) {
        return map.get(key);
    }

    public DocumentWeightSerial serialize(MapVisitor mv) {
        return mv.visit(this);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public int size() {
        return map.size();
    }

    public void print() {
        for (String key : map.keySet()) {
            System.out.println(key + ":" + map.get(key));
        }
    }

}
