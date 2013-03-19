package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.mysema.query.Connections;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.testutil.ExternalDB;

@Category(ExternalDB.class)
public class PostgreSQLSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initPostgres();
        Connections.setTemplates(new PostgresTemplates(true) {{
            newLineToSingleSpace();
        }});
    }
    
}
