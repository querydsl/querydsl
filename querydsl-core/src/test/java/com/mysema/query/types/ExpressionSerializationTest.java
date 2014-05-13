package com.mysema.query.types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class ExpressionSerializationTest {

    @Test
    public void Serialize() throws ClassNotFoundException, IOException {
        QTuple e = new QTuple(new StringPath("x"), new NumberPath(Integer.class, "y"));
        serialize(e);
        serialize(e.newInstance("a",1));
    }

    private void serialize(Object obj) throws IOException, ClassNotFoundException{
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytesOut);
        out.writeObject(obj);
        out.close();
        bytesOut.close();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bytesIn);
        Object obj2 = in.readObject();
        obj2.hashCode();
    }

}
