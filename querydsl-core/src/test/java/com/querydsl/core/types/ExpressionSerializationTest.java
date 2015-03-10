package com.querydsl.core.types;

import java.io.IOException;

import org.junit.Test;

import com.querydsl.core.testutil.Serialization;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;

public class ExpressionSerializationTest {

    @Test
    public void Serialize() throws ClassNotFoundException, IOException {
        QTuple e = new QTuple(new StringPath("x"), new NumberPath(Integer.class, "y"));
        serialize(e);
        serialize(e.newInstance("a",1));
    }

    private void serialize(Object obj) throws IOException, ClassNotFoundException{
        Object obj2 = Serialization.serialize(obj);
        obj2.hashCode();
    }

}
