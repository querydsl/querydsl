package com.mysema.query.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Test;

import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.util.ReflectionUtils;

public class ReflectionUtilsTest {

    @Nullable
    String property;

    @Test
    public void GetAnnotatedElement() {
        AnnotatedElement annotatedElement = ReflectionUtils.getAnnotatedElement(ReflectionUtilsTest.class, "property", String.class);
        assertNotNull(annotatedElement.getAnnotation(Nullable.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void GetImplementedInterfaces() {
        Set<Class<?>> ifaces = ReflectionUtils.getImplementedInterfaces(SimpleExpression.class);
        assertEquals(new HashSet<Class<?>>(Arrays.asList(Serializable.class, Expression.class)), ifaces);
    }

}
