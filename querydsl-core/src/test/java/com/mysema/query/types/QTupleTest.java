package com.mysema.query.types;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class QTupleTest {
    
    @Test
    public void test(){
        StringPath str1 = new StringPath("str1");
        StringPath str2 = new StringPath("str2");
        StringPath str3 = new StringPath("str3");
        StringPath str4 = new StringPath("str4");
        Expression<?>[] exprs1 = new Expression[]{str1, str2};
        Expression<?>[] exprs2 = new Expression[]{str3, str4};
        
        assertEquals(Arrays.asList(str1, str2), new QTuple(str1, str2).getArgs());
        assertEquals(Arrays.asList(str1, str2), new QTuple(exprs1).getArgs());
        assertEquals(Arrays.asList(str1, str2, str3, str4), new QTuple(exprs1, exprs2).getArgs());
    }

}
