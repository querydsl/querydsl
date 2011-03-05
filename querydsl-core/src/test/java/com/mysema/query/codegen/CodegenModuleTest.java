package com.mysema.query.codegen;

import static org.junit.Assert.*;

import org.junit.Test;

public class CodegenModuleTest {
    
    private CodegenModule module = new CodegenModule();
    
    @Test
    public void DefaultPrefix(){
        assertEquals("Q", module.get(String.class, CodegenModule.PREFIX));
    }
    
    @Test
    public void TypeMappings(){
        assertNotNull(module.get(TypeMappings.class));
    }

}
