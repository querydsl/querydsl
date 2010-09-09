/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._derby;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.SkipForQuoted;
import com.mysema.query.Target;
import com.mysema.query.sql.DerbyTemplates;
import com.mysema.testutil.Label;

@Label(Target.DERBY)
@SkipForQuoted
public class SelectDerbyQuotedTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initDerby();
    }

    @Before
    public void setUpForTest() {
        templates = new DerbyTemplates(true).newLineToSingleSpace();
    }

}
