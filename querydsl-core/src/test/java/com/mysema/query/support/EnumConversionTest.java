package com.mysema.query.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.EnumPath;

public class EnumConversionTest {

    public enum Color {GREEN, BLUE, RED, YELLOW, BLACK, WHITE}

    @Test
    public void Name() {
        EnumPath<Color> color = new EnumPath<Color>(Color.class, "path");
        EnumConversion<Color> conv = new EnumConversion<Color>(color);
        assertEquals(Color.BLUE, conv.newInstance("BLUE"));
    }

    @Test
    public void Ordinal() {
        EnumPath<Color> color = new EnumPath<Color>(Color.class, "path");
        EnumConversion<Color> conv = new EnumConversion<Color>(color);
        assertEquals(Color.RED, conv.newInstance(2));
    }
}
