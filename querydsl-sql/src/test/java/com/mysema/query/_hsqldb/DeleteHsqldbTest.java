/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._hsqldb;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.DeleteBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.HSQLDBTemplates;
import com.mysema.testutil.Label;

@Label(Target.HSQLDB)
public class DeleteHsqldbTest extends DeleteBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initHSQL();
    }

    @Before
    public void setUp() throws SQLException {
        dialect = new HSQLDBTemplates().newLineToSingleSpace();
        super.setUp();
    }

}
