package com.querydsl.jpa;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.types.Expression;

public class ExpressionSerializationTest {

    private <T> T serialize(T obj) throws ClassNotFoundException, IOException {
        // serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(obj);
        out.close();

        // deserialize
        ByteArrayInputStream bain = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bain);
        T obj2 = (T) in.readObject();
        in.close();
        return obj2;
    }

    @Test
    public void Serialize() throws Exception {
        //QAdress.adress.name.eq("test"
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
