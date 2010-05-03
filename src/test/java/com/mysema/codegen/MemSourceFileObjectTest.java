package com.mysema.codegen;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MemSourceFileObjectTest {
    
    @Test
    public void test(){
        MemSourceFileObject obj = new MemSourceFileObject("Test", "Hello World");
        assertEquals("Hello World", obj.getCharContent(true).toString());
    }

    @Test
    public void testOpenWriter() throws IOException {
        MemSourceFileObject obj = new MemSourceFileObject("Test");
        obj.openWriter().write("Hello World");
        assertEquals("Hello World", obj.getCharContent(true).toString());
    }
    
    @Test
    public void testOpenWriter2() throws IOException {
        MemSourceFileObject obj = new MemSourceFileObject("Test");
        obj.openWriter().append("Hello World");
        assertEquals("Hello World", obj.getCharContent(true).toString());
    }

}
