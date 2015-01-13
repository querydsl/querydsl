package com.querydsl.core.types;

import java.io.*;

import com.querydsl.core.types.path.SimplePath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SerializationTest {

    @Test
    public void roundtrip() throws Exception {
        PathImpl path = new PathImpl(Object.class, "entity");
        SimplePath path2 = new SimplePath(Object.class, "entity");
        assertEquals(path, roundtrip(path));
        assertEquals(path2, roundtrip(path2));
        assertEquals(path2.isNull(), roundtrip(path2.isNull()));
        assertEquals(path.hashCode(), roundtrip(path).hashCode());
        assertEquals(path2.hashCode(), roundtrip(path2).hashCode());
        assertEquals(path2.isNull().hashCode(), roundtrip(path2.isNull()).hashCode());
    }

    private <T> T roundtrip(T obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(obj);
        out.close();

        // deserialize predicate
        ByteArrayInputStream bain = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bain);
        return (T) in.readObject();
    }

}
