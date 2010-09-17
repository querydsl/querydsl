/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import org.junit.Test;

import com.mysema.query.types.expr.ConstructorExpression;

public class ConstructorExpressionTest {

    public static class Projection{

        public Projection(){

        }

        public Projection(Long id){

        }

        public Projection(long id, String text){

        }

        public Projection(CharSequence text){

        }
    }

    @Test
    public void test_Constructor(){
        Expression<Long> longVal = ConstantImpl.create(1l);
        Expression<String> stringVal = ConstantImpl.create("");
        new ConstructorExpression<Projection>(Projection.class, new Class[]{long.class, String.class}, longVal, stringVal).newInstance(0l,"");
    }

    @Test
    public void test_create(){
        Expression<Long> longVal = ConstantImpl.create(1l);
        Expression<String> stringVal = ConstantImpl.create("");
        ConstructorExpression.create(Projection.class, longVal, stringVal).newInstance(0l,"");
    }

    @Test
    public void test_create2(){
        Expression<Long> longVal = ConstantImpl.create(1l);
        ConstructorExpression.create(Projection.class, longVal).newInstance(0l);
    }

    @Test
    public void test_create3(){
        ConstructorExpression.create(Projection.class).newInstance();
    }

    @Test
    public void test_create4(){
        Expression<String> stringVal = ConstantImpl.create("");
        ConstructorExpression.create(Projection.class, stringVal).newInstance("");
    }

}
