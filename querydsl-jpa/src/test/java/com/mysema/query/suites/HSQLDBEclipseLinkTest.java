package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Mode;
import com.mysema.query.Target;

public class HSQLDBEclipseLinkTest extends AbstractJPASuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("hsqldb-eclipselink");
        Mode.target.set(Target.HSQLDB);
    }

}
