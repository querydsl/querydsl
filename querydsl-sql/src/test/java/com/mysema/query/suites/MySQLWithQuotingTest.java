package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.mysema.query.Connections;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.testutil.ExternalDB;

@Category(ExternalDB.class)
public class MySQLWithQuotingTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initMySQL();
        Connections.setTemplates(new MySQLTemplates(true) {{
            newLineToSingleSpace();
        }});
    }
    
}
