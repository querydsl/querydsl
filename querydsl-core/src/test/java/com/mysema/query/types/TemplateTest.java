package com.mysema.query.types;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.Template;


public class TemplateTest {

    @Test
    public void test(){
        match("[0, ' + ', 1, ' + ', 2]", new Template("{0} + {1} + {2}"));
        match("['blah ', 0, ' + ', 1, ' + ', 2, ' XXX']", new Template("blah {0} + {1} + {2} XXX"));
        match("['+', 1]",                new Template("+{1}"));
        match("[1, '.method()']",        new Template("{1}.method()"));        
        match("[0, '.get(', 1, ')']",    new Template("{0}.get({1})"));        
        match("[0, '.', 1s]",            new Template("{0}.{1s}"));
    }

    private void match(String string, Template template) {
        assertEquals(string, template.getElements().toString());        
    }
    
}
