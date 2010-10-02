/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import org.junit.Test;


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
    public void Constructor(){
        Expression<Long> longVal = ConstantImpl.create(1l);
        Expression<String> stringVal = ConstantImpl.create("");
        new ConstructorExpression<Projection>(Projection.class, new Class[]{long.class, String.class}, longVal, stringVal).newInstance(0l,"");
    }

    @Test
    public void Create(){
        Expression<Long> longVal = ConstantImpl.create(1l);
        Expression<String> stringVal = ConstantImpl.create("");
        ConstructorExpression.create(Projection.class, longVal, stringVal).newInstance(0l,"");
    }

    @Test
    public void Create2(){
        Expression<Long> longVal = ConstantImpl.create(1l);
        ConstructorExpression.create(Projection.class, longVal).newInstance(0l);
    }

    @Test
    public void Create3(){
        ConstructorExpression.create(Projection.class).newInstance();
    }

    @Test
    public void Create4(){
        Expression<String> stringVal = ConstantImpl.create("");
        ConstructorExpression.create(Projection.class, stringVal).newInstance("");
    }

}
