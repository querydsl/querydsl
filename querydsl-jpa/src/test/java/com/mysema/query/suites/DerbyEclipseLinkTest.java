package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Mode;
import com.mysema.query.Target;

public class DerbyEclipseLinkTest extends AbstractJPASuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("derby-eclipselink");
        Mode.target.set(Target.DERBY);
    }
}
