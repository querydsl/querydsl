package com.mysema.query.types.expr;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class ComparableExpressionTest {
    
    private StringPath strPath = new StringPath("str");
    
    @Test
    public void Between_Start_Given() {
        assertEquals(strPath.goe("A"), strPath.between("A", null));        
    }
    
    @Test
    public void Between_End_Given() {
        assertEquals(strPath.loe("Z"), strPath.between(null, "Z"));
    }

}
