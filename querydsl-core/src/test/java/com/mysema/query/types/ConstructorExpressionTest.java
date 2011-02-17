/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;


public class ConstructorExpressionTest {

    StringPath str1 = new StringPath("str1");
    StringPath str2 = new StringPath("str2");
    StringPath str3 = new StringPath("str3");
    Concatenation concat = new Concatenation(str1, str2);

    @Test
    public void Constructor(){
        Expression<Long> longVal = ConstantImpl.create(1l);
        Expression<String> stringVal = ConstantImpl.create("");
        assertNotNull(new ConstructorExpression<ProjectionExample>(ProjectionExample.class, new Class[]{long.class, String.class}, longVal, stringVal).newInstance(0l,""));
    }

    @Test
    public void Create(){
        Expression<Long> longVal = ConstantImpl.create(1l);
        Expression<String> stringVal = ConstantImpl.create("");
        assertNotNull(ConstructorExpression.create(ProjectionExample.class, longVal, stringVal).newInstance(0l,""));
    }

    @Test
    public void Create2(){
        Expression<Long> longVal = ConstantImpl.create(1l);
        assertNotNull(ConstructorExpression.create(ProjectionExample.class, longVal).newInstance(0l));
    }

    @Test
    public void Create3(){
        assertNotNull(ConstructorExpression.create(ProjectionExample.class).newInstance());
    }

    @Test
    public void Create4(){
        Expression<String> stringVal = ConstantImpl.create("");
        assertNotNull(ConstructorExpression.create(ProjectionExample.class, stringVal).newInstance(""));
    }

    @Test
    public void FactoryExpression_has_right_args(){
        FactoryExpression<ProjectionExample> constructor = ConstructorExpression.create(ProjectionExample.class, concat);
        constructor = FactoryExpressionUtils.wrap(constructor);
        assertEquals(Arrays.asList(str1, str2), constructor.getArgs());
    }

    @Test
    public void FactoryExpression_newInstance(){
        FactoryExpression<ProjectionExample> constructor = ConstructorExpression.create(ProjectionExample.class, concat);
        constructor = FactoryExpressionUtils.wrap(constructor);
        ProjectionExample projection = constructor.newInstance("12","34");
        assertEquals("1234", projection.text);
    }

}
