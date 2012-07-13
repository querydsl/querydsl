package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.OracleTemplates;

public class OracleWithQuotingTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initOracle();
        Connections.setTemplates(new OracleTemplates(true){{
            newLineToSingleSpace();
        }});
    }
    
}
