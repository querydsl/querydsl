package com.mysema.query._mysql;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.CreateTableBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@ResourceCheck("/mysql.run")
@Label(Target.MYSQL)
public class CreateTableMySQLTest extends CreateTableBaseTest{
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initMySQL();
    }

    @Before
    public void setUp() throws SQLException {
        super.setUp();
        templates = new MySQLTemplates(true).newLineToSingleSpace();
    }

}