package com.david.cedict.util;

import com.david.cedict.struct.DocumentWeightMap;
import com.david.cedict.struct.DocumentWeightSerial;
import com.david.cedict.struct.PostingListMap;
import com.david.cedict.struct.PostingListSerial;

public class MapVisitor implements Visitor {

    // Serialize
    @Override
    public PostingListSerial visit(PostingListMap map) {
        String s = "";
        for (int id: map.keySet()) {
            s += "#" + id + ":" + map.get(id);
        }
        return new PostingListSerial(s);
    }

    // Deserialize
    @Override
    public PostingListMap visit(PostingListSerial serial) {
        PostingListMap map = new PostingListMap();
        int i = 0;
        while (i < serial.getData().length()) {
            if (serial.getData().charAt(i) == '#') {
                i++;
                String docId = "";
                while (i < serial.getData().length() && serial.getData().charAt(i) != ':') {
                    docId += serial.getData().charAt(i);
                    i++;
                }
                i++;
                String freq = "";
                while (i < serial.getData().length() && serial.getData().charAt(i) != '#') {
                    freq += serial.getData().charAt(i);
                    i++;
                }
                map.put(Integer.parseInt(docId), Integer.parseInt(freq));
            } else {
                System.out.println("Deserializer string not as expected.");
            }
        }
        return map;
    }

    // Serialize
    @Override
    public DocumentWeightSerial visit(DocumentWeightMap map) {
        String s = "";
        for (String word: map.keySet()) {
            s += "#" + word + ":" + map.get(word);
        }
        return new DocumentWeightSerial(s);
    }

    // Deserialize
    @Override
    public DocumentWeightMap visit(DocumentWeightSerial serial) {
        DocumentWeightMap map = new DocumentWeightMap();
        int i = 0;
        while (i < serial.getData().length()) {
            if (serial.getData().charAt(i) == '#') {
                i++;
                String word = "";
                while (i < serial.getData().length() && serial.getData().charAt(i) != ':') {
                    word += serial.getData().charAt(i);
                    i++;
                }
                i++;
                String weight = "";
                while (i < serial.getData().length() && serial.getData().charAt(i) != '#') {
                    weight += serial.getData().charAt(i);
                    i++;
                }
                map.put(word, Float.parseFloat(weight));
            } else {
                System.out.println("Deserializer string not as expected.");
            }
        }
        return map;
    }
}

