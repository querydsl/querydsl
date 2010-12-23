/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._oracle;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.SkipForQuoted;
import com.mysema.query.Target;
import com.mysema.query.sql.OracleTemplates;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@ResourceCheck("/oracle.run")
@Label(Target.ORACLE)
@SkipForQuoted
public class SelectOracleQuotedTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initOracle();
    }

    @Before
    public void setUpForTest() {
        templates = new OracleTemplates(true).newLineToSingleSpace();
    }

}
