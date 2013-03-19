package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.HSQLDBTemplates;

public class HsqldbSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initHSQL();
        Connections.setTemplates(new HSQLDBTemplates() {{
            newLineToSingleSpace();
        }});
    }
    
}
