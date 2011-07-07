/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


public class StringTest {

    @SuppressWarnings("unchecked")
    @Test
    public void PatternAvailability() throws IllegalArgumentException, IllegalAccessException{
        Templates ops = new Templates(){{
            // TODO
        }};
        Set<Field> missing = new HashSet<Field>();
        for (Field field : Ops.class.getFields()){
            if (field.getType().equals(OperatorImpl.class)){
                Operator op = (Operator)field.get(null);
                if (ops.getTemplate(op) == null) missing.add(field);
            }
        }
        for (Class<?> cl : Ops.class.getClasses()){
            for (Field field : cl.getFields()){
                if (field.getType().equals(OperatorImpl.class)){
                    Operator op = (Operator)field.get(null);
                    if (ops.getTemplate(op) == null) missing.add(field);
                }
            }
        }

        if (!missing.isEmpty()){
            for (Field field : missing){
                System.err.println(field.getName());
            }
            fail();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void ToString(){
        SomeType alias = alias(SomeType.class, "alias");

        // Path toString
        assertEquals("alias.name", $(alias.getName()).toString());
        assertEquals("alias.ref.name", $(alias.getRef().getName()).toString());
        assertEquals("alias.refs.get(0)", $(alias.getRefs().get(0)).toString());

        // Operation toString
        assertEquals("lower(alias.name)", $(alias.getName()).lower().toString());

        // ConstructorExpression
        ConstructorExpression<SomeType> someType = new ConstructorExpression<SomeType>(SomeType.class, new Class[]{SomeType.class}, $(alias));
        assertEquals("new SomeType(alias)", someType.toString());

        // ArrayConstructorExpression
        ArrayConstructorExpression<SomeType> someTypeArray = new ArrayConstructorExpression<SomeType>(SomeType[].class,$(alias));
        assertEquals("new SomeType[](alias)", someTypeArray.toString());
    }


    public static class SomeType{

        public SomeType(){}
        
        public SomeType(SomeType st){}
        
        public String getName(){return ""; };

        public SomeType getRef(){ return null; };

        public List<SomeType> getRefs() { return null; };

        public int getAmount() { return 0; };
    }

}
