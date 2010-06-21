/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._postgres;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.query.Target;
import com.mysema.query.UpdateBaseTest;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@RunWith(FilteringTestRunner.class)
@ResourceCheck("/postgres.run")
@Label(Target.POSTGRES)
public class UpdatePostgresTest extends UpdateBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initPostgres();
    }

    @Before
    public void setUp() throws SQLException {
        dialect = new PostgresTemplates().newLineToSingleSpace();
        super.setUp();
    }

}
