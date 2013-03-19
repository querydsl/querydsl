package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.SQLiteTemplates;

public class SQLiteSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initSQLite();
        Connections.setTemplates(new SQLiteTemplates() {{
            newLineToSingleSpace();
        }});
    }
    
}
