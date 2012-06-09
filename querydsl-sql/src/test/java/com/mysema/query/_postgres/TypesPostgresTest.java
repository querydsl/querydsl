package com.mysema.query._postgres;

import org.junit.BeforeClass;
import org.junit.Ignore;

import com.mysema.query.Connections;
import com.mysema.query.TypesBaseTest;

// FIXME
@Ignore
public class TypesPostgresTest extends TypesBaseTest {
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initPostgres();
    }
    
}
