/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._postgres;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.Target;
import com.mysema.query.UpdateBaseTest;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

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
