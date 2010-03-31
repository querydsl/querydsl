package com.mysema.query.types.expr;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.path.PString;

public class CoalesceTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void withList(){
        PString firstname = new PString("firstname");
        PString lastname = new PString("lastname");
        Coalesce<String> c = new Coalesce<String>(firstname, lastname).add("xxx");
        assertEquals("coalesce(firstname, lastname, xxx)", c.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void withSingleArg(){
        Coalesce<String> c = new Coalesce<String>().add("xxx");
        assertEquals("coalesce(xxx)", c.toString());
    }
}
