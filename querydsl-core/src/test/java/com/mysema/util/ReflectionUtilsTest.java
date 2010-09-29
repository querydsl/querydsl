package com.mysema.util;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;


public class ReflectionUtilsTest {
    
    @Test
    public void getImplementedInterfaces(){
        assertEquals(
            new HashSet<Class<?>>(Arrays.<Class<?>>asList(Appendable.class, CharSequence.class, Serializable.class)), 
            ReflectionUtils.getImplementedInterfaces(StringBuilder.class));
        
        assertEquals(
            new HashSet<Class<?>>(Arrays.<Class<?>>asList(Collection.class, Iterable.class)), 
            ReflectionUtils.getImplementedInterfaces(List.class));
    }

}
