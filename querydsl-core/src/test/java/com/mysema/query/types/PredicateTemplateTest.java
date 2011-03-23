package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PredicateTemplateTest {

    @Test
    public void Not() {
        PredicateTemplate template = new PredicateTemplate("XXX");
        assertEquals("!XXX", template.not().toString());
    }

}
