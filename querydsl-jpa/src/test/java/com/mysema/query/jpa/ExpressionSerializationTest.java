package com.mysema.query.jpa;

import static com.mysema.testutil.Serialization.serialize;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Expression;

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
        JPAQuery query = new JPAQuery();
        query.from(QCat.cat);
        query.where(serialize(QCat.cat.name.eq("test")));
    }

}
