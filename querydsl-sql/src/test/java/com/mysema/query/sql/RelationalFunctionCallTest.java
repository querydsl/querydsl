package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.path.StringPath;

public class RelationalFunctionCallTest {
    
    @Test
    public void NoArgs() {
        RelationalFunctionCall<String> functionCall = RelationalFunctionCall.create(String.class, "getElements");
        assertEquals("getElements()", functionCall.getTemplate().toString());
    }
    
    @Test
    public void TwoArgs() {
        StringPath str = new StringPath("str");
        RelationalFunctionCall<String> functionCall = RelationalFunctionCall.create(String.class, "getElements", "a", str);
        assertEquals("getElements({0}, {1})", functionCall.getTemplate().toString());
        assertEquals(ConstantImpl.create("a"), functionCall.getArg(0));
        assertEquals(str, functionCall.getArg(1));        
    }
    
}
