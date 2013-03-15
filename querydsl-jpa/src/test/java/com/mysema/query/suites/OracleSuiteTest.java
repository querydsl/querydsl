package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.mysema.query.Mode;
import com.mysema.query.Target;
import com.mysema.testutil.ExternalDB;

@Category(ExternalDB.class)
public class OracleSuiteTest extends AbstractSuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("oracle");
        Mode.target.set(Target.ORACLE);
    }

}
