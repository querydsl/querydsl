package com.mysema.query.alias;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScalaAccessorsTest {
    
    @Test
    public void ScalaAccessors(){
        ScalaAccessors scalaAccessors = Alias.alias(ScalaAccessors.class);
        assertEquals("scalaAccessors.firstName", $(scalaAccessors.firstName()).toString());
        assertEquals("scalaAccessors.lastName",  $(scalaAccessors.lastName()).toString());
    }

}
