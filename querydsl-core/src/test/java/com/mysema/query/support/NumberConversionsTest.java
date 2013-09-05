package com.mysema.query.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.Tuple;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class NumberConversionsTest {

    @Test
    public void Safe_Number_Conversion() {
        StringPath strPath = new StringPath("strPath");
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "intPath");
        QTuple qTuple = new QTuple(strPath, intPath);
        NumberConversions<Tuple> conversions = new NumberConversions<Tuple>(qTuple);
        assertNotNull(conversions.newInstance(1, 2));
    }

    @Test
    public void Number_Conversion() {
        StringPath strPath = new StringPath("strPath");
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "intPath");
        QTuple qTuple = new QTuple(strPath, intPath);
        NumberConversions<Tuple> conversions = new NumberConversions<Tuple>(qTuple);
        Tuple tuple = conversions.newInstance("a", Long.valueOf(3));
        assertEquals("a", tuple.get(strPath));
        assertEquals(Integer.valueOf(3), tuple.get(intPath));
    }

}
