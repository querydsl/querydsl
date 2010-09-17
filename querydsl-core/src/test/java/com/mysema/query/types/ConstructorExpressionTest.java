/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import org.junit.Test;

import com.mysema.query.NumberConstant;
import com.mysema.query.StringConstant;
import com.mysema.query.types.expr.ConstructorExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;

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
        NumberExpression<Long> longVal = NumberConstant.create(1l);
        StringExpression stringVal = StringConstant.create("");
        new ConstructorExpression<Projection>(Projection.class, new Class[]{long.class, String.class}, longVal, stringVal).newInstance(0l,"");
    }

    @Test
    public void test_create(){
        NumberExpression<Long> longVal = NumberConstant.create(1l);
        StringExpression stringVal = StringConstant.create("");
        ConstructorExpression.create(Projection.class, longVal, stringVal).newInstance(0l,"");
    }

    @Test
    public void test_create2(){
        NumberExpression<Long> longVal = NumberConstant.create(1l);
        ConstructorExpression.create(Projection.class, longVal).newInstance(0l);
    }

    @Test
    public void test_create3(){
        ConstructorExpression.create(Projection.class).newInstance();
    }

    @Test
    public void test_create4(){
        StringExpression stringVal = StringConstant.create("");
        ConstructorExpression.create(Projection.class, stringVal).newInstance("");
    }

}
