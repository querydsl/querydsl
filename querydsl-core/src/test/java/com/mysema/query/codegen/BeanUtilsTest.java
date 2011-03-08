package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BeanUtilsTest {

    @Test
    public void Capitalize() {
        assertEquals("Prop", BeanUtils.capitalize("prop"));
        assertEquals("URL",  BeanUtils.capitalize("URL"));
        assertEquals("cId",  BeanUtils.capitalize("cId"));
    }

    @Test
    public void Uncapitalize() {
        assertEquals("prop", BeanUtils.uncapitalize("Prop"));
        assertEquals("URL",  BeanUtils.uncapitalize("URL"));
        assertEquals("cId",  BeanUtils.uncapitalize("cId"));
    }

}
