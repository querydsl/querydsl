package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.MySQLTemplates;

public class MySQLWithQuotingTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initMySQL();
        Connections.setTemplates(new MySQLTemplates(true){{
            newLineToSingleSpace();
        }});
    }
    
}
