package com.querydsl.core.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.QTuple;
import com.querydsl.core.types.path.EnumPath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;

public class NumberConversionsTest {


    public enum Color {GREEN, BLUE, RED, YELLOW, BLACK, WHITE}

    @Test
    public void Name() {
        EnumPath<Color> color = new EnumPath<Color>(Color.class, "path");
        QTuple qTuple = new QTuple(color);
        NumberConversions<Tuple> conversions = new NumberConversions<Tuple>(qTuple);
        assertEquals(Color.BLUE, conversions.newInstance("BLUE").get(color));
    }

    @Test
    public void Ordinal() {
        EnumPath<Color> color = new EnumPath<Color>(Color.class, "path");
        QTuple qTuple = new QTuple(color);
        NumberConversions<Tuple> conversions = new NumberConversions<Tuple>(qTuple);
        assertEquals(Color.RED, conversions.newInstance(2).get(color));
    }

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
