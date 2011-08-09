package com.mysema.util;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import com.SomeClass;


public class ClassPathUtilsTest {
    
    @Test
    public void ScanPackage() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> classes = ClassPathUtils.scanPackage(classLoader, SomeClass.class.getPackage());
        assertFalse(classes.isEmpty());
    }

}
