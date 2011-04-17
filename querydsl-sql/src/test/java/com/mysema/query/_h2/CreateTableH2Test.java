/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._h2;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.CreateTableBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.H2Templates;
import com.mysema.testutil.Label;

@Label(Target.H2)
public class CreateTableH2Test extends CreateTableBaseTest{
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initH2();
    }

    @Before
    public void setUp() throws SQLException {
        super.setUp();
        templates = new H2Templates(){{
            newLineToSingleSpace();
        }};
    }

}
