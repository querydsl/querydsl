/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._oracle;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.ExportBaseTest;
import com.mysema.testutil.ResourceCheck;

@ResourceCheck("/oralce.run")
public class ExportOracleTest extends ExportBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initOracle();
    }

}
