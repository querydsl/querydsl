package com.mysema.query.mysql;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.DeleteBaseTest;
import com.mysema.query.sql.MySQLTemplates;

public class DeleteMySQLTest extends DeleteBaseTest{
    
    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initMySQL();
    }

    @Before
    public void setUpForTest() {
        dialect = new MySQLTemplates().newLineToSingleSpace();
    }

}
