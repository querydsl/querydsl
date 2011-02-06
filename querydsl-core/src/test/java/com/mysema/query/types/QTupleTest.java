package com.mysema.query.types;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class QTupleTest {
    
    StringPath str1 = new StringPath("str1");
    StringPath str2 = new StringPath("str2");
    StringPath str3 = new StringPath("str3");
    StringPath str4 = new StringPath("str4");
    Expression<?>[] exprs1 = new Expression[]{str1, str2};
    Expression<?>[] exprs2 = new Expression[]{str3, str4};
    
    Concatenation concat = new Concatenation(str1, str2);
    
    @Test
    public void TwoExpressions_getArgs(){        
        assertEquals(Arrays.asList(str1, str2), new QTuple(str1, str2).getArgs());                
    }
    
    @Test
    public void OneArray_getArgs(){
        assertEquals(Arrays.asList(str1, str2), new QTuple(exprs1).getArgs());
    }
    
    @Test
    public void TwoExpressionArrays_getArgs(){
        assertEquals(Arrays.asList(str1, str2, str3, str4), new QTuple(exprs1, exprs2).getArgs());
    }
    
    @Test
    public void NestedProjection_getArgs(){        
        assertEquals(Arrays.asList(str1, str2), new QTuple(concat).getArgs());
    }
    
    @Test
    public void NestedProjection_getArgs2(){        
        assertEquals(Arrays.asList(str1, str2, str3), new QTuple(concat, str3).getArgs());
    }
    
    @Test
    public void NestedProjection_newInstance(){
        QTuple expr = new QTuple(concat);
        assertEquals("1234", expr.newInstance("12", "34").get(concat));
    }

}
