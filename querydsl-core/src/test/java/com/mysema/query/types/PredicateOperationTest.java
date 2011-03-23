package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PredicateOperationTest {

    @SuppressWarnings("unchecked")
    @Test
    public void Not() {
        Path o1 = new PathImpl(Object.class, "o1");
        Path o2 = new PathImpl(Object.class, "o2");
        PredicateOperation template = new PredicateOperation(Ops.EQ_OBJECT, o1, o2);
        assertEquals("!o1 = o2", template.not().toString());
    }

}
