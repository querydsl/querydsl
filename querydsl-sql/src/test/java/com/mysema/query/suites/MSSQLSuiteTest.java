package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.Ignore;

import com.mysema.query.Connections;
import com.mysema.query.sql.SQLServer2005Templates;

@Ignore
public class MSSQLSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initSQLServer();
        Connections.setTemplates(SQLServer2005Templates.builder().newLineToSingleSpace().build());
    }

}
