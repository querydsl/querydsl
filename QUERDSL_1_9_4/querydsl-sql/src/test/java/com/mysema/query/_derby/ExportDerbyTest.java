/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._derby;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.ExportBaseTest;

public class ExportDerbyTest extends ExportBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initDerby();
    }

}
