package com.david.cedict;

import com.david.cedict.struct.*;
import com.david.cedict.util.MapVisitor;
import junit.framework.TestCase;

import java.util.Map;

public class SerialTest extends TestCase {
    public void writeSerialTest(DBMap map, DBSerial expectedSerial) {
        String observed = map.serialize(new MapVisitor()).getData();
        if (!observed.equals(expectedSerial.getData())) {
            fail("Serial mismatch. Expected: " + expectedSerial.getData() + ", Actual: " + observed);
        }
    }

    public void testSerial() {
        PostingListMap postingListMap = new PostingListMap(Map.of(1,2,3,4));
        writeSerialTest(postingListMap, new PostingListSerial("#1:2#3:4"));

        DocumentWeightMap documentWeightMap = new DocumentWeightMap(Map.of("London", Float.valueOf((float)0.5), "Taipei", Float.valueOf((float)0.99), "Seoul", Float.valueOf((float)0.23)));
        writeSerialTest(documentWeightMap, new DocumentWeightSerial("#Seoul:0.23#Taipei:0.99#London:0.5"));
    }
}
