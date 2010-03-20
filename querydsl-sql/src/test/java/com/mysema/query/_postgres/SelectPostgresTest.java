/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query._postgres;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

// ignored, because the tables are created in upper case and Postgres normalizes unquoted identifiers to lower case
@RunWith(FilteringTestRunner.class)
@ResourceCheck("DO.NOT.RUN")
@Label(Target.POSTGRES)
public class SelectPostgresTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initPostgres();
    }

    @Before
    public void setUpForTest() {
        dialect = new PostgresTemplates().newLineToSingleSpace();
    }


}
