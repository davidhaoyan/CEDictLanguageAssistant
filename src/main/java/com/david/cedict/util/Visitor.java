package com.david.cedict.util;

import com.david.cedict.struct.DocumentWeightMap;
import com.david.cedict.struct.DocumentWeightSerial;
import com.david.cedict.struct.PostingListMap;
import com.david.cedict.struct.PostingListSerial;

public interface Visitor {
    public PostingListSerial visit(PostingListMap map);
    public PostingListMap visit(PostingListSerial serial);
    public DocumentWeightSerial visit(DocumentWeightMap map);
    public DocumentWeightMap visit(DocumentWeightSerial serial);

}
