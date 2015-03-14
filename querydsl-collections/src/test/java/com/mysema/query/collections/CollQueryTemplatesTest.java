package com.mysema.query.collections;

import org.junit.Test;

import com.mysema.query.types.TemplatesTestUtils;

public class CollQueryTemplatesTest {

    @Test
    public void Generic_Precedence() {
        TemplatesTestUtils.testPrecedence(CollQueryTemplates.DEFAULT);
    }

}
