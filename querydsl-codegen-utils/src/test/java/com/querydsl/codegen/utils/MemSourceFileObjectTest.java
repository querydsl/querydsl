/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.querydsl.codegen.utils;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MemSourceFileObjectTest {

    @Test
    public void Simple() {
        MemSourceFileObject obj = new MemSourceFileObject("Test", "Hello World");
        assertEquals("Hello World", obj.getCharContent(true).toString());
    }

    @Test
    public void OpenWriter() throws IOException {
        MemSourceFileObject obj = new MemSourceFileObject("Test");
        obj.openWriter().write("Hello World");
        assertEquals("Hello World", obj.getCharContent(true).toString());
    }

    @Test
    public void OpenWriter2() throws IOException {
        MemSourceFileObject obj = new MemSourceFileObject("Test");
        obj.openWriter().append("Hello World");
        assertEquals("Hello World", obj.getCharContent(true).toString());
    }

}
