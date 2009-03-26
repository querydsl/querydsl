package com.mysema.query.grammar.types;

import static com.mysema.query.grammar.GrammarWithAlias.$;
import static com.mysema.query.grammar.GrammarWithAlias.alias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Expr.EArrayConstructor;
import com.mysema.query.grammar.types.Expr.EConstructor;
import com.mysema.query.serialization.OperationPatterns;

/**
 * StringTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class StringTest {
    
    @Test
    public void testPatternAvailability() throws IllegalArgumentException, IllegalAccessException{
        OperationPatterns ops = new OperationPatterns(){{
            // TODO
        }};
        Set<Field> missing = new HashSet<Field>();
        for (Field field : Ops.class.getFields()){
            Op op = (Op)field.get(null);
            if (ops.getPattern(op) == null) missing.add(field);
        }
        for (Class<?> cl : Ops.class.getClasses()){
            for (Field field : cl.getFields()){
                Op op = (Op)field.get(null);
                if (ops.getPattern(op) == null) missing.add(field);
            }   
        }
        
        if (!missing.isEmpty()){
            for (Field field : missing){
                System.err.println(field.getName());
            }
            fail();
        }
    }
    
    @Test
    public void testToString(){
        SomeType alias = alias(SomeType.class, "alias");
        
        // Path toString
        assertEquals("alias.name", $(alias.getName()).toString());
        assertEquals("alias.ref.name", $(alias.getRef().getName()).toString());
        assertEquals("alias.refs.get(0)", $(alias.getRefs().get(0)).toString());
        
        // Operation toString
        assertEquals("lower(alias.name)", $(alias.getName()).lower().toString());
        
        // EConstructor
        EConstructor<SomeType> someType = new EConstructor<SomeType>(SomeType.class,$(alias));
        assertEquals("new SomeType(alias)", someType.toString());
        
        // EArrayConstructor
        EArrayConstructor<SomeType> someTypeArray = new EArrayConstructor<SomeType>(SomeType.class,$(alias));
        assertEquals("[alias]", someTypeArray.toString());
    }
    
    public interface SomeType{        
        String getName();
        SomeType getRef();
        List<SomeType> getRefs();
        int getAmount();
    }

}
