package com.mysema.query.suites;

import com.mysema.query.JPABase;
import com.mysema.query.JPASQLBase;
import com.mysema.query.Mode;
import com.mysema.query.Target;
import com.mysema.query.jpa.JPAIntegrationBase;
import com.mysema.query.jpa.SerializationBase;
import org.junit.BeforeClass;
import org.junit.Ignore;

@Ignore
public class H2BatooTest extends AbstractJPASuite {

    public static class JPA extends JPABase {}
    public static class JPASQL extends JPASQLBase {}
    public static class JPAIntegration extends JPAIntegrationBase {}
    public static class Serialization extends SerializationBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("h2-batoo");
        Mode.target.set(Target.H2);
    }

}
