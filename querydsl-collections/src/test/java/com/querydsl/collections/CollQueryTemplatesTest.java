package com.querydsl.collections;

import org.junit.Test;

import com.querydsl.core.types.TemplatesTestUtils;

public class CollQueryTemplatesTest {

    @Test
    public void Generic_Precedence() {
        TemplatesTestUtils.testPrecedence(CollQueryTemplates.DEFAULT);
    }

}
