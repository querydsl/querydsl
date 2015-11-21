package com.querydsl.core.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QTuple;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public class NumberConversionsTest {

    public enum Color { GREEN, BLUE, RED, YELLOW, BLACK, WHITE }

    @Test
    public void name() {
        EnumPath<Color> color = Expressions.enumPath(Color.class, "path");
        QTuple qTuple = Projections.tuple(color);
        NumberConversions<Tuple> conversions = new NumberConversions<Tuple>(qTuple);
        assertEquals(Color.BLUE, conversions.newInstance("BLUE").get(color));
    }

    @Test
    public void ordinal() {
        EnumPath<Color> color = Expressions.enumPath(Color.class, "path");
        QTuple qTuple = Projections.tuple(color);
        NumberConversions<Tuple> conversions = new NumberConversions<Tuple>(qTuple);
        assertEquals(Color.RED, conversions.newInstance(2).get(color));
    }

    @Test
    public void safe_number_conversion() {
        StringPath strPath = Expressions.stringPath("strPath");
        NumberPath<Integer> intPath = Expressions.numberPath(Integer.class, "intPath");
        QTuple qTuple = Projections.tuple(strPath, intPath);
        NumberConversions<Tuple> conversions = new NumberConversions<Tuple>(qTuple);
        assertNotNull(conversions.newInstance(1, 2));
    }

    @Test
    public void number_conversion() {
        StringPath strPath = Expressions.stringPath("strPath");
        NumberPath<Integer> intPath = Expressions.numberPath(Integer.class, "intPath");
        QTuple qTuple = Projections.tuple(strPath, intPath);
        NumberConversions<Tuple> conversions = new NumberConversions<Tuple>(qTuple);
        Tuple tuple = conversions.newInstance("a", Long.valueOf(3));
        assertEquals("a", tuple.get(strPath));
        assertEquals(Integer.valueOf(3), tuple.get(intPath));
    }

}
