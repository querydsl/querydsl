/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;



public class TemplateTest {

    @Test
    public void test(){
        TemplateFactory factory = new TemplateFactory();
        match("[0, ' + ', 1, ' + ', 2]", factory.create("{0} + {1} + {2}"));
        match("['blah ', 0, ' + ', 1, ' + ', 2, ' XXX']", factory.create("blah {0} + {1} + {2} XXX"));
        match("['+', 1]",                factory.create("+{1}"));
        match("[1, '.method()']",        factory.create("{1}.method()"));        
        match("[0, '.get(', 1, ')']",    factory.create("{0}.get({1})"));        
        match("[0, '.', 1s]",            factory.create("{0}.{1s}"));        
    }
    
    @Test
    public void like(){
	TemplateFactory factory = new TemplateFactory();
	match("[0]",                     factory.create("{0%}"));
	match("[0]",                     factory.create("{%0}"));
	match("[0]",                     factory.create("{%0%}"));
	
	match("[0]",                     factory.create("{0%%}"));
	match("[0]",                     factory.create("{%%0}"));
	match("[0]",                     factory.create("{%%0%%}"));
    }

    private void match(String string, Template template) {
        assertEquals(string, template.getElements().toString());        
    }
    
}

