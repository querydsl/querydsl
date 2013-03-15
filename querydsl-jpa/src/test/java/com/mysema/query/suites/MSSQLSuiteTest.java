package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;

import com.mysema.query.Mode;
import com.mysema.query.Target;
import com.mysema.testutil.ExternalDB;

@Ignore
@Category(ExternalDB.class)
public class MSSQLSuiteTest extends AbstractSuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("mssql");
        Mode.target.set(Target.SQLSERVER);
    }

}
