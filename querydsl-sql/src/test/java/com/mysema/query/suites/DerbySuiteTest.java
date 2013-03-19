package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.DerbyTemplates;

public class DerbySuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initDerby();
        Connections.setTemplates(new DerbyTemplates() {{
            newLineToSingleSpace();
        }});
    }
    
}
