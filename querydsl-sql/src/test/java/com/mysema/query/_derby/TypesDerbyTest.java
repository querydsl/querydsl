package com.mysema.query._derby;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.TypesBaseTest;

public class TypesDerbyTest extends TypesBaseTest {
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initDerby();
    }
    
}
