/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.sql.HSQLDBTemplates;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;

/**
 * HsqdlbTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
@RunWith(FilteringTestRunner.class)
@Label(Target.HSQLDB)
public class SelectHsqldbTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initHSQL();
    }

    @Before
    public void setUpForTest() {
        dialect = new HSQLDBTemplates().newLineToSingleSpace();
    }

}
