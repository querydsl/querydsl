package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.PString;

@SuppressWarnings("unchecked")
public class CoalesceTest {
    
    PString firstname = new PString("firstname");
    
    PString lastname = new PString("lastname");
    
    @Test
    public void withList(){        
        Coalesce<String> c = new Coalesce<String>(firstname, lastname).add("xxx");
        assertEquals("coalesce(firstname, lastname, xxx)", c.toString());
    }
    
    @Test
    public void withSingleArg(){
        Coalesce<String> c = new Coalesce<String>().add("xxx");
        assertEquals("coalesce(xxx)", c.toString());
    }
    
    @Test
    public void asComparable(){
        Coalesce<String> c = new Coalesce<String>(firstname, lastname);       
        c.asc();
    }
    
    @Test
    public void asString(){
        Coalesce<String> c = new Coalesce<String>(firstname, lastname);
        c.asString().lower();
    }
}
