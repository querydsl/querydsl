/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._h2;

import static com.mysema.query.Constants.employee;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.Wildcard;
import com.mysema.query.types.path.PNumber;
import com.mysema.testutil.Label;

@Label(Target.H2)
public class SelectH2QuotedTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initH2();
    }

    @Before
    public void setUpForTest() {
        templates = new H2Templates(true).newLineToSingleSpace();
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
