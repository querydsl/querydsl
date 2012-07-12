package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.PostgresTemplates;

public class PostgreSQLSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initPostgres();
        Connections.setTemplates(new PostgresTemplates(true){{
            newLineToSingleSpace();
        }});
    }
    
}
