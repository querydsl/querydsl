/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._oracle;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.InsertBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.OracleTemplates;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@ResourceCheck("/oracle.run")
@Label(Target.ORACLE)
public class InsertOracleTest extends InsertBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initOracle();
    }

    @Before
    public void setUp() throws SQLException {
        templates = new OracleTemplates(){{
            newLineToSingleSpace();
        }};
        super.setUp();
    }

}
