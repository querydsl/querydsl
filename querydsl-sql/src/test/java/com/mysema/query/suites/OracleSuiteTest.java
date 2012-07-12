package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.OracleTemplates;

public class OracleSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initOracle();
        Connections.setTemplates(new OracleTemplates(){{
            newLineToSingleSpace();
        }});
    }
    
}
