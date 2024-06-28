package com.david.cedict.struct;

import com.david.cedict.util.MapVisitor;

public class DocumentWeightSerial extends DBSerial {
    public DocumentWeightSerial(String s) {
        data = s;
    }

    @Override
    public DBMap deserialize(MapVisitor mv) {
        return mv.visit(this);
    }
}
