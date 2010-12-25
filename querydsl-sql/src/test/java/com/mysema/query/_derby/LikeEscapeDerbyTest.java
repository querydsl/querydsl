/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._derby;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.LikeEscapeBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.DerbyTemplates;
import com.mysema.testutil.Label;

@Label(Target.DERBY)
public class LikeEscapeDerbyTest extends LikeEscapeBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initDerby();
    }

    @Before
    public void setUp() throws SQLException {
        templates = new DerbyTemplates('!', false).newLineToSingleSpace();
        super.setUp();
    }

}
