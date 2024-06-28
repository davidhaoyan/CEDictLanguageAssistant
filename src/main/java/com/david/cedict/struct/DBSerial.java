package com.david.cedict.struct;

import com.david.cedict.util.MapVisitor;

public abstract class DBSerial {
    protected String data = null;

    public abstract DBMap deserialize(MapVisitor mv);
    public String getData() {
        return data;
    }
}
