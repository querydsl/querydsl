package com.querydsl.core.types;

import static com.querydsl.core.testutil.Serialization.serialize;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;

public class SerializationTest {

    @Test
    public void roundtrip() throws Exception {
        Path<?> path = ExpressionUtils.path(Object.class, "entity");
        SimplePath<?> path2 = Expressions.path(Object.class, "entity");
        assertEquals(path, serialize(path));
        assertEquals(path2, serialize(path2));
        assertEquals(path2.isNull(), serialize(path2.isNull()));
        assertEquals(path.hashCode(), serialize(path).hashCode());
        assertEquals(path2.hashCode(), serialize(path2).hashCode());
        assertEquals(path2.isNull().hashCode(), serialize(path2.isNull()).hashCode());
    }

}
