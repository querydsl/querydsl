/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.annotation.Nullable;

import org.junit.AfterClass;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.SQLTemplates;

public abstract class AbstractBaseTest {
   
    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        Connections.close();            
    }
    
    protected SQLTemplates dialect;
    
    @Nullable
    protected String expectedQuery;    

    protected SQLQuery query() {
        return new SQLQueryImpl(Connections.getConnection(), dialect) {
            @Override
            protected String buildQueryString(boolean countRow) {
                String rv = super.buildQueryString(countRow);
                if (expectedQuery != null) {
                    assertEquals(expectedQuery, rv);
                    expectedQuery = null;
                }
                System.out.println(rv);
                return rv;
            }
        };
    }
    

    protected SQLSubQuery s(){
        return new SQLSubQuery();
    }


    
}
