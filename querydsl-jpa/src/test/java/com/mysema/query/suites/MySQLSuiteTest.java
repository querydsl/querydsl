package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.mysema.query.Mode;
import com.mysema.query.Target;
import com.mysema.testutil.ExternalDB;

@Category(ExternalDB.class)
public class MySQLSuiteTest extends AbstractSuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("mysql");
        Mode.target.set(Target.MYSQL);
    }

}
