package com.mysema.query._h2;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import com.mysema.query.AbstractJPATest;
import com.mysema.query.Target;
import com.mysema.query.jpa.EclipseLinkTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

@Ignore
@RunWith(JPATestRunner.class)
@JPAConfig("h2-openjpa")
public class H2JPAOpenJPATest extends AbstractJPATest {
    
    @Override
    protected JPQLTemplates getTemplates(){
        return EclipseLinkTemplates.DEFAULT;
    }

    @Override
    protected Target getTarget() {
        return Target.H2;
    }

}
