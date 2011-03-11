package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CodegenModuleTest {

    private final CodegenModule module = new CodegenModule();

    @Test
    public void DefaultPrefix(){
        assertEquals("Q", module.get(String.class, CodegenModule.PREFIX));
    }

    @Test
    public void TypeMappings(){
        assertNotNull(module.get(TypeMappings.class));
    }

    @Test(expected=IllegalArgumentException.class)
    public void Get_With_Unknown_Key(){
        module.get(String.class, "XXX");
    }

}
