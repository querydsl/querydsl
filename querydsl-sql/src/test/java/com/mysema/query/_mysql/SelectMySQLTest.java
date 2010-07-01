/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._mysql;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@ResourceCheck("/mysql.run")
@Label(Target.MYSQL)
public class SelectMySQLTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initMySQL();
    }

    @Before
    public void setUpForTest() {
        dialect = new MySQLTemplates().newLineToSingleSpace();
    }

}
