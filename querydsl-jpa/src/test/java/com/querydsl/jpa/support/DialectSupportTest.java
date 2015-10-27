package com.querydsl.jpa.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Template;
import com.querydsl.sql.HSQLDBTemplates;

public class DialectSupportTest {

    @Test
    public void Convert() {
        Template trim = HSQLDBTemplates.DEFAULT.getTemplate(Ops.TRIM);
        assertEquals("trim(both from ?1)", DialectSupport.convert(trim));
        Template concat = HSQLDBTemplates.DEFAULT.getTemplate(Ops.CONCAT);
        assertEquals("?1 || ?2", DialectSupport.convert(concat));
    }
}
