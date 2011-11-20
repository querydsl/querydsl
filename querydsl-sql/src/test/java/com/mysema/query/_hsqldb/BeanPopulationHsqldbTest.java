package com.mysema.query._hsqldb;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.BeanPopulationBaseTest;
import com.mysema.query.Connections;
import com.mysema.query.Target;
import com.mysema.query.sql.HSQLDBTemplates;
import com.mysema.testutil.Label;

@Label(Target.HSQLDB)
public class BeanPopulationHsqldbTest extends BeanPopulationBaseTest{
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initH2();
    }

    @Before
    public void setUp() throws SQLException {
        templates = new HSQLDBTemplates(){{
            newLineToSingleSpace();
        }};
    }

}
