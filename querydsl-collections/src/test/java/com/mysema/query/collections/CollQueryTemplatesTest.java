package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.TemplatesTestUtils;
import com.mysema.query.types.path.StringPath;

public class CollQueryTemplatesTest {

    @Test
    public void Generic_Precedence() {
        TemplatesTestUtils.testPrecedence(CollQueryTemplates.DEFAULT);
    }

    @Test
    public void Concat() {
        StringPath a = Expressions.stringPath("a");
        StringPath b = Expressions.stringPath("b");
        Expression<?> expr = a.append(b).toLowerCase();
        String str = new CollQuerySerializer(CollQueryTemplates.DEFAULT).handle(expr).toString();
        assertEquals("(a + b).toLowerCase()", str);
    }

    @Test
    public void Concat_ContainsIgnoreCase() {
        StringPath a = Expressions.stringPath("a");
        StringPath b = Expressions.stringPath("b");
        Expression<?> expr = a.append(" ").append(b).containsIgnoreCase("test");
        String str = new CollQuerySerializer(CollQueryTemplates.DEFAULT).handle(expr).toString();
        assertEquals("(a + a1 + b).toLowerCase().contains(a2)", str);
    }
}
