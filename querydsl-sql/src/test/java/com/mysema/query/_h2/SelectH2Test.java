/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._h2;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.H2Templates;
import com.mysema.testutil.Label;

@Label(Target.H2)
public class SelectH2Test extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initH2();
    }

    @Before
    public void setUpForTest() {
        templates = new H2Templates().newLineToSingleSpace();
    }

}
