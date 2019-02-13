package com.querydsl.core.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;

public class EnumConversionTest {

    public enum Color { GREEN, BLUE, RED, YELLOW, B, W }

    @Test
    public void nameForCharacter() {
        EnumPath<Color> color = Expressions.enumPath(Color.class, "path");
        EnumConversion<Color> conv = new EnumConversion<Color>(color);
        assertEquals(Color.W, conv.newInstance('W'));
    }

    @Test
    public void name() {
        EnumPath<Color> color = Expressions.enumPath(Color.class, "path");
        EnumConversion<Color> conv = new EnumConversion<Color>(color);
        assertEquals(Color.BLUE, conv.newInstance("BLUE"));
    }

    @Test
    public void ordinal() {
        EnumPath<Color> color = Expressions.enumPath(Color.class, "path");
        EnumConversion<Color> conv = new EnumConversion<Color>(color);
        assertEquals(Color.RED, conv.newInstance(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegal() {
        StringPath string = Expressions.stringPath("path");
        EnumConversion<String> conv = new EnumConversion<String>(string);
        fail("EnumConversion successfully created for a non-enum type");
        conv.newInstance(0);
    }
}
