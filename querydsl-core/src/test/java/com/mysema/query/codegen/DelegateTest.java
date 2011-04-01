package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;

import org.junit.Test;

import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Types;

public class DelegateTest {

    @Test
    public void Equals_Object() {
        Delegate delegate = new Delegate(Types.STRING, Types.STRING, "delegate", Collections.<Parameter>emptyList(), Types.STRING);
        Delegate delegate2 = new Delegate(Types.STRING, Types.STRING, "delegate", Collections.<Parameter>emptyList(), Types.STRING);
        assertEquals(delegate, delegate2);
    }

    @Test
    public void Not_Equals_Object() {
        Delegate delegate = new Delegate(Types.STRING, Types.STRING, "delegate", Collections.<Parameter>emptyList(), Types.STRING);
        Delegate delegate2 = new Delegate(Types.STRING, Types.STRING, "delegate2", Collections.<Parameter>emptyList(), Types.STRING);
        assertFalse(delegate.equals(delegate2));
    }

}
