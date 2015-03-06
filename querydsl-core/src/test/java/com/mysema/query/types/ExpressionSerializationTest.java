package com.mysema.query.types;

import java.io.IOException;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.testutil.Serialization;

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
