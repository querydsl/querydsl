/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.oracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;

public class OracleGrammarTest {

    @Test
    public void constants(){
        assertNotNull(OracleGrammar.level);
        assertNotNull(OracleGrammar.rownum);
        assertNotNull(OracleGrammar.sysdate);
    }

    @Test
    public void sumOver(){
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "intPath");
        SumOver<Integer> sumOver = OracleGrammar.sumOver(intPath).order(intPath).partition(intPath);
        assertEquals("sum(intPath) over (partition by intPath order by intPath)", sumOver.toString());
    }

}
