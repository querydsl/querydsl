package com.mysema.query.jpa.sql;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.sql.DerbyTemplates;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
@JPAConfig("derby")
public class DerbyJPASQLTest extends AbstractJPASQLTest {

    public DerbyJPASQLTest() {
        super(new DerbyTemplates());
    }
    
    @Test
    @Override
    public void Union2() {
        // not supported in Derby
    }
    
    
    @Test
    @Override
    public void Union3() {
        // not supported in Derby
    }
    
}
