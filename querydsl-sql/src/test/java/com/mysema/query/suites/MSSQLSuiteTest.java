package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.Ignore;

import com.mysema.query.Connections;
import com.mysema.query.sql.SQLServerTemplates;

@Ignore 
public class MSSQLSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initSQLServer();
        Connections.setTemplates(new SQLServerTemplates() {{
            newLineToSingleSpace();
        }});
    }
    
}
