/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._hsqldb;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.HSQLDBTemplates;
import com.mysema.testutil.Label;

@Label(Target.HSQLDB)
public class SelectHsqldbQuotedTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initHSQL();
    }

    @Before
    public void setUpForTest() {
        templates = new HSQLDBTemplates(true).newLineToSingleSpace();
    }

    @Override
    public void limitAndOffset2() throws SQLException {

    }

    @Override
    public void serialization(){

    }

    @Override
    public void subQueries() throws SQLException {

    }
    
    @Test
    public void wildcardAll() {

    }

    @Test
    public void countAll() {
        
    }
    
    @Test
    public void path_alias(){
        
    }
    
}
