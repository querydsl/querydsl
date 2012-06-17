package com.mysema.query._sqlite;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.Target;
import com.mysema.query.TypesBaseTest;
import com.mysema.testutil.Label;

@Label(Target.SQLITE)
public class TypesSQLiteTest extends TypesBaseTest {
    
    // not supported
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initSQLite();
    }
    
}
