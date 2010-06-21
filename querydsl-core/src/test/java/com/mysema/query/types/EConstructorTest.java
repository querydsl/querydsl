/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.Test;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;

public class EConstructorTest {

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
        ENumber<Long> longVal = ENumberConst.create(1l);
        EString stringVal = EStringConst.create("");
        new EConstructor<Projection>(Projection.class, new Class[]{long.class, String.class}, longVal, stringVal).newInstance(0l,"");
    }

    @Test
    public void test_create(){
        ENumber<Long> longVal = ENumberConst.create(1l);
        EString stringVal = EStringConst.create("");
        EConstructor.create(Projection.class, longVal, stringVal).newInstance(0l,"");
    }

    @Test
    public void test_create2(){
        ENumber<Long> longVal = ENumberConst.create(1l);
        EConstructor.create(Projection.class, longVal).newInstance(0l);
    }

    @Test
    public void test_create3(){
        EConstructor.create(Projection.class).newInstance();
    }

    @Test
    public void test_create4(){
        EString stringVal = EStringConst.create("");
        EConstructor.create(Projection.class, stringVal).newInstance("");
    }

}
