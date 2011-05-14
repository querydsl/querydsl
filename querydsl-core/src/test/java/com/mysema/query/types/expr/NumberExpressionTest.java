package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;

public class NumberExpressionTest {
    
    private NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "int");
    
    @Test
    public void Between_Start_Given() {
        assertEquals(intPath.goe(1), intPath.between(1, null));        
    }
    
    @Test
    public void Between_End_Given() {
        assertEquals(intPath.loe(3), intPath.between(null, 3));
    }

}
