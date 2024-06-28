package com.david.cedict;

import com.david.cedict.struct.*;
import com.david.cedict.util.MapVisitor;
import junit.framework.TestCase;

import java.util.Map;

public class DeserialTest extends TestCase {
    public void writeDeserialTest(DBSerial serial, DBMap expectedMap) {
        DBMap observed = serial.deserialize(new MapVisitor());
        for (Object key: expectedMap.keySet()) {
            if (!observed.keySet().contains(key)) {
                fail("Observed DBMap does not contain key: " + key + "of expected DBMap");
            }
            else if (!observed.get(key).equals(expectedMap.get(key))) {
                fail("Expected " + key + ":" + expectedMap.get(key) + ", Actual " + key + ":" + observed.get(key));
            }
        }
    }

    public void testDeserial() {
        writeDeserialTest(new PostingListSerial("#1:3#2:4"), new PostingListMap(Map.of(Integer.valueOf(1),Integer.valueOf(3),Integer.valueOf(2),Integer.valueOf(4))));
        writeDeserialTest(new DocumentWeightSerial("#Hanoi:0.82#Saigon:0.1"), new DocumentWeightMap(Map.of("Hanoi", Float.valueOf((float)0.82), "Saigon", Float.valueOf((float)0.1))));
    }
}
