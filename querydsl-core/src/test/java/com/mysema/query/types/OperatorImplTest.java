package com.mysema.query.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.testutil.Serialization;

public class OperatorImplTest {

    @Test
    public void GetId() {
        assertEquals("com.mysema.query.types.Ops#ALIAS", Ops.ALIAS.getId());
    }

    @Test
    public void Serialization() {
        Operator<?> operator = Serialization.serialize(Ops.ADD);
        assertTrue(operator == Ops.ADD);
    }
}
