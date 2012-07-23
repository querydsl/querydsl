package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.Ignore;

import com.mysema.query.Mode;
import com.mysema.query.Target;

@Ignore
public class H2OpenJPATest extends AbstractJPASuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("h2-openjpa");
        Mode.target.set(Target.H2);
    }

}
