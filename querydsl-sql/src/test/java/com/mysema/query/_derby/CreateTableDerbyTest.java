package com.mysema.query._derby;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import com.mysema.query.Connections;
import com.mysema.query.CreateTableBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.DerbyTemplates;
import com.mysema.testutil.Label;

@Label(Target.DERBY)
@Ignore
public class CreateTableDerbyTest extends CreateTableBaseTest{
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initDerby();
    }

    @Before
    public void setUp() throws SQLException {
        super.setUp();
        templates = new DerbyTemplates().newLineToSingleSpace();
    }

}
