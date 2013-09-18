package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.OracleTemplates;

//@Category(ExternalDB.class)
public class OracleSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initOracle();
        Connections.setTemplates(OracleTemplates.builder().newLineToSingleSpace().build());
    }

}
