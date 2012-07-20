package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Mode;
import com.mysema.query.Target;

public class OracleSuiteTest extends AbstractSuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("oracle");
        Mode.target.set(Target.ORACLE);
    }

}
