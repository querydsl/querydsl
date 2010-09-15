/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.util.AnnotatedElementAdapter;

public class PathTest {

    public static class Superclass {

        @Nullable
        public String getProperty4(){
            return null;
        }
    }

    @QueryEntity
    public static class Entity extends Superclass{

        @Nullable
        private String property1;

        private String property2;

        @QueryTransient
        private String property3;

        public String getProperty1(){
            return property1;
        }

        @Nonnull
        public String getProperty2(){
            return property2;
        }

        @Nonnull
        public String getProperty3(){
            return property3;
        }

    }

    @Test
    public void getAnnotatedElement(){
        Entity entity = Alias.alias(Entity.class);

        AnnotatedElement element = $(entity).getAnnotatedElement();

        // type
        assertEquals(Entity.class, element);
    }

    @Test
    public void getAnnotatedElement_for_property(){
        Entity entity = Alias.alias(Entity.class);
        AnnotatedElement property1 = $(entity.getProperty1()).getAnnotatedElement();
        AnnotatedElement property2 = $(entity.getProperty2()).getAnnotatedElement();
        AnnotatedElement property3 = $(entity.getProperty3()).getAnnotatedElement();
        AnnotatedElement property4 = $(entity.getProperty4()).getAnnotatedElement();

        // property (field)
        assertEquals(Field.class, property1.getClass());
        assertTrue(property1.isAnnotationPresent(Nullable.class));
        assertNotNull(property1.getAnnotation(Nullable.class));
        assertFalse(property1.isAnnotationPresent(Nonnull.class));
        assertNull(property1.getAnnotation(Nonnull.class));

        // property2 (method)
        assertEquals(Method.class, property2.getClass());
        assertTrue(property2.isAnnotationPresent(Nonnull.class));
        assertNotNull(property2.getAnnotation(Nonnull.class));
        assertFalse(property2.isAnnotationPresent(Nullable.class));
        assertNull(property2.getAnnotation(Nullable.class));

        // property3 (both)
        assertEquals(AnnotatedElementAdapter.class, property3.getClass());
        assertTrue(property3.isAnnotationPresent(QueryTransient.class));
        assertNotNull(property3.getAnnotation(QueryTransient.class));
        assertTrue(property3.isAnnotationPresent(Nonnull.class));
        assertNotNull(property3.getAnnotation(Nonnull.class));
        assertFalse(property3.isAnnotationPresent(Nullable.class));
        assertNull(property3.getAnnotation(Nullable.class));

        // property 4 (superclass)
        assertEquals(Method.class, property4.getClass());
        assertTrue(property4.isAnnotationPresent(Nullable.class));
        assertNotNull(property4.getAnnotation(Nullable.class));

    }
    
    @Test
    public void equals(){
        assertEquals(new StringPath("s"),  new StringPath("s"));
        assertEquals(new BooleanPath("b"), new BooleanPath("b"));
        assertEquals(new NumberPath<Integer>(Integer.class,"n"), new NumberPath<Integer>(Integer.class,"n"));
        
    }
    
}
