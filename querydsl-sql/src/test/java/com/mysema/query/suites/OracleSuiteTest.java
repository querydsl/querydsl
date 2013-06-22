package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.mysema.query.Connections;
import com.mysema.query.sql.OracleTemplates;
import com.mysema.testutil.ExternalDB;

@Category(ExternalDB.class)
public class OracleSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initOracle();
        Connections.setTemplates(OracleTemplates.builder().newLineToSingleSpace().build());
    }

}
