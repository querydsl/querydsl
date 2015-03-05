package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OperatorImplTest {

    @Test
    public void GetId() {
        assertEquals("com.mysema.query.types.Ops#ALIAS", Ops.ALIAS.getId());
    }
}
