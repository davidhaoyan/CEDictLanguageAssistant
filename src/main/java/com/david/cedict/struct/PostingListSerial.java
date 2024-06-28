package com.david.cedict.struct;

import com.david.cedict.util.MapVisitor;

public class PostingListSerial extends DBSerial {
    public PostingListSerial(String s) {
        data = s;
    }

    public PostingListMap deserialize(MapVisitor mv) {
        return mv.visit(this);
    }
}
