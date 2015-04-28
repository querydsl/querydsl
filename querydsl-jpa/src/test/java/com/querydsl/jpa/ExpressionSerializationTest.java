package com.querydsl.jpa;

import static com.querydsl.core.testutil.Serialization.serialize;
import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.jpa.domain.QCat;

public class ExpressionSerializationTest {

    @Test
    public void Serialize() throws Exception {
        Expression<?> expr = QCat.cat.name.eq("test");
        Expression<?> expr2 = serialize(expr);

        assertEquals(expr, expr2);
        assertEquals(expr.hashCode(), expr2.hashCode());
    }

    @Test
    public void Query() throws ClassNotFoundException, IOException {
        selectFrom(QCat.cat).where(serialize(QCat.cat.name.eq("test")));
    }

}
