package com.mysema.codegen;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Writer;

import javax.tools.JavaFileObject.Kind;

import org.junit.Test;

public class MemJavaFileObjectTest {

    @Test
    public void testOpenOutputStream() throws IOException {
        MemJavaFileObject obj = new MemJavaFileObject("mem","Test",Kind.SOURCE);
        Writer writer = obj.openWriter();
        writer.write("Hello World");
        writer.flush();
        writer.close();
        assertEquals("Hello World", obj.getCharContent(true).toString());
    }

}
