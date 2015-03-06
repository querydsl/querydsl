package com.mysema.query.types;

import static com.mysema.testutil.Serialization.serialize;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.SimplePath;

public class SerializationTest {

    @Test
    public void roundtrip() throws Exception {
        PathImpl path = new PathImpl(Object.class, "entity");
        SimplePath path2 = new SimplePath(Object.class, "entity");
        assertEquals(path, serialize(path));
        assertEquals(path2, serialize(path2));
        assertEquals(path2.isNull(), serialize(path2.isNull()));
        assertEquals(path.hashCode(), serialize(path).hashCode());
        assertEquals(path2.hashCode(), serialize(path2).hashCode());
        assertEquals(path2.isNull().hashCode(), serialize(path2.isNull()).hashCode());
    }

}
