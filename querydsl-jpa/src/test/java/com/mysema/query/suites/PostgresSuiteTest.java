package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Mode;
import com.mysema.query.Target;

public class PostgresSuiteTest extends AbstractSuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("postgres");
        Mode.target.set(Target.POSTGRES);
    }

}
